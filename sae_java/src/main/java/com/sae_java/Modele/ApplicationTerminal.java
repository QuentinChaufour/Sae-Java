package com.sae_java.Modele;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.sae_java.Modele.Enumerations.EnumInfoClient;
import com.sae_java.Modele.Enumerations.EnumPalmares;
import com.sae_java.Modele.Enumerations.EnumUpdatesDB;
import com.sae_java.Modele.Exceptions.BookNotInStockException;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Exceptions.NoCorrespondingClient;
import com.sae_java.Modele.Exceptions.QuantiteInvalideException;

public class ApplicationTerminal {

    private Client client;        // Représente le client connecté
    private Vendeur vendeur;      // Représente le vendeur connecté


    private final Scanner scanner;

    public ApplicationTerminal() {
        // Initialisation de l'application
        this.scanner = new Scanner(System.in);
        this.client = null;
        this.vendeur = null;
    }

    /**
     * gestion de l'identification de l'utilisateur
     */
    public void menuIdentification() {
        boolean actif = true;
        
        while (actif) {
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                         IDENTIFICATION                         ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║   Veuillez choisir votre rôle :                                ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║  A. Client                                                     ║");
            System.out.println("║  B. Vendeur                                                    ║");
            System.out.println("║  C. Administrateur                                             ║");
            System.out.println("║  D. Quitter                                                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
            System.out.print("Votre choix : ");

            String choix = this.scanner.nextLine().trim().toLowerCase();
            switch (choix) {
                case "a" -> {
                    actif = false;
                    this.menuIdentificationClient();
                }
                case "b" -> {
                    System.out.println("Cette option n'est pas implémenté pour l'instant");
                }
                case "c" -> {
                    actif = false;
                    menuIdentificationAdmin();
                }
                case "d" -> {
                    actif = false;
                    this.quitter();
                }
                default -> System.out.println("Choix invalide.");
                
            }
        }
    }

    /**
     * permet de gérer l'identification d'un client
     */
    public void menuIdentificationClient() {

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     IDENTIFICATION  CLIENT                     ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        System.out.println("Veuillez entrer vos identifiants : \n");
        System.out.print("Nom : ");
        String nom = this.scanner.nextLine().trim();

        System.out.print("Prénom : ");
        String prenom = this.scanner.nextLine().trim();

        System.out.print("Mot de passe : ");
        String motDePasse = this.scanner.nextLine().trim();

        try {
            this.client = Reseau.identificationClient(nom, prenom, motDePasse, null);

            // Si l'identification est réussie, on affiche le menu client
            System.out.println("Identification réussie. \nBienvenue " + this.client.getPrenom() + " " + this.client.getNom() + " !");

            boolean actifLibrairie = true;
            while (actifLibrairie) {
                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║                       Choix de Librairie                       ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  Veuillez choisir votre librairie :                            ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");


                this.page(Reseau.librairies,null);

                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  A. Retour au menu d'identification                            ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
                System.out.print("Votre choix : ");
                String choixLibrairie = this.scanner.nextLine().trim().toLowerCase();

                switch(choixLibrairie){

                    case "a" -> {
                        actifLibrairie = false;
                        this.menuIdentification();
                    }

                    default -> {
                        int idLibrairie = Integer.parseInt(choixLibrairie);
                        if (idLibrairie >= 1 && idLibrairie <= Reseau.librairies.size()) {
                            actifLibrairie = false;
                            this.client.setLibrairie(idLibrairie - 1); // 1 de diff avec l'affichage
                        } 
                        else {
                            System.out.println("Veuillez entrer un numéro de librairie valide.");
                        }
                    }
                }
            }

        } catch (NoCorrespondingClient e) {
            System.out.println("Identifiants incorrects.");
            System.out.println(e.getMessage());
            this.menuIdentification();
        } catch (SQLException e) {
            System.out.println("Une erreur est survenue lors de l'identification.");
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un numéro de librairie valide.");
        }

        this.menuPClient();
    }

    /**
     * permet de gérer l'identification d'un admin
     */
    public void menuIdentificationAdmin() {

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                      IDENTIFICATION ADMIN                      ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝\n");
        System.out.println("Veuillez entrer vos identifiants : \n");
        System.out.print("Identifiant : ");
        String user = this.scanner.nextLine().trim();

        System.out.print("Mot de passe : ");
        String motDePasse = this.scanner.nextLine().trim();

        if(!Reseau.identificationAdmin(user, motDePasse)){
            System.out.println("L'identifiant ou le mot de passe est incorrecte !");
            this.menuIdentification();
        }
        else{
            this.menuPAdmin();
        }
    }


    /**
     * gére le menu principal pour les clients
     * 
     */
    public void menuPClient() {

        boolean actif = true;

        while(actif){
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                         MENU PRINCIPAL                         ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║   Que voulez vous faire :                                      ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║  A. Consulter catalogue                                        ║");
            System.out.println("║  B. Se faire recommander des livres                            ║");
            System.out.println("║  C. Changer librairie                                          ║");
            System.out.println("║  D. Consulter Panier                                           ║");
            System.out.println("║  E. Modification information personnelles                      ║");
            System.out.println("║  F. Déconnexion                                                ║");
            System.out.println("║  Q. Quitter                                                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");

            System.out.print("Votre choix : ");

            String choix = this.scanner.nextLine().trim().toLowerCase();

            switch (choix) {
                case "a" -> {
                    try {
                        this.menuConsultationBooks();
                    } 
                    catch (LibraryNotFoundException e) {
                        System.out.println("Erreur : " + e.getMessage());
                    }
                }

                case "b" -> {
                    this.menuRecommandation();
                }

                case "c" -> {
                    this.changementLibClient();
                }
                case "d" -> {
                    this.menuConsultationPanier();
                }
                case "e" -> {
                    this.menuUpdateInfoClient();
                }
                case "f" -> {
                    actif = false;
                    this.deconnexion();
                }
                case "q" -> {
                    actif = false;
                    this.quitter();
                }
                default -> System.out.println("Choix invalide. \n Veuillez réessayer.");
            }
        }
        

    }

    /**
     * gére le menu principal pour l'administrateur
     */
    public void menuPAdmin(){
        boolean actif = true;

        while(actif){
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                         MENU PRINCIPAL                         ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║   Que voulez vous faire :                                      ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║  A. Consulter et modifier les stocks                           ║");
            System.out.println("║  B. Consulter les statistiques                                 ║");
            // créer un compte vendeur
            System.out.println("║  F. Déconnexion                                                ║");
            System.out.println("║  Q. Quitter                                                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");

            System.out.print("Votre choix : ");

            String choix = this.scanner.nextLine().trim().toLowerCase();

            switch (choix) {
                case "a" -> {
                    this.menuConsultationBooksAdmin();
                }

                case "b" -> {
                    this.menuStats();
                }

                case "f" -> {
                    actif = false;
                    this.deconnexion();
                }
                case "q" -> {
                    actif = false;
                    this.quitter();
                }
                default -> System.out.println("Choix invalide. \n Veuillez réessayer.");
            }
        }
    }

    /**
     * permet de consutler les livres en stocks de la librairie courrante
     * @throws LibraryNotFoundException
     */
    public void menuConsultationBooks() throws LibraryNotFoundException{

        int page = 0; // page de base 
        boolean actif = true;

        while (actif) {
            Reseau.updateInfos(EnumUpdatesDB.STOCKS);
            List<Livre> livres = new ArrayList<>(Reseau.getLibrairie(this.client.getLibrairie()).consulterStock().keySet()); // en liste pour pouvoir trier les livres
            Collections.sort(livres); // Tri des livres selon ISBN

            int pageMax = livres.size()/5 == 0 ? livres.size()/5 : (int)Math.ceil(livres.size()/5); // la dernière page est remplie ou non 

            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                    CONSULTATION DES LIVRES                     ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣ ");

            this.page(livres.subList(page * 5, Math.min((page + 1) * 5, livres.size())),this.client.getLibrairie());

            String pageString = "  Page " + (page + 1) + " / " + (pageMax + 1);
            System.out.println("║                                                                ║"); // espacement
            System.out.println("║ " + pageString + " ".repeat(62 - pageString.length()) + " ║");

            System.out.println("╠════════════════════════════════════════════════════════════════╣");

            System.out.println("║  Veuillez choisir une action :                                 ║");

            System.out.println("║  Saisissez le numéro d'un livre pour l'ajouter au panier       ║");

            if( page > 0) {
                System.out.println("║  A. Page précédente                                            ║");
            } 
            else {
                System.out.println("║  A. Page précédente (désactivée)                               ║");
            }

            if (page < pageMax) {
                System.out.println("║  B. Page suivante                                              ║");
            }
            else {
                System.out.println("║  B. Page suivante (désactivée)                                 ║");
            }
            System.out.println("║  C. Information supplémentaire livre                           ║");
            System.out.println("║  D. Changer de librairie                                       ║");
            System.out.println("║  E. Consulter le panier                                        ║");
            System.out.println("║  F. Retour au menu principal                                   ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
            System.out.print("Votre choix : ");
            
            String choix = this.scanner.nextLine().trim().toLowerCase();

            switch (choix) {
                case "a" -> {
                    if (page > 0) {
                        page--;
                    }
                    else {
                        System.out.println("Vous êtes déjà à la première page.");
                    }
                }
                case "b" -> {
                    if (page < pageMax) {
                        page++;
                    }
                    else {
                        System.out.println("Vous êtes déjà à la dernière page.");
                    }
                }
                case "c" -> {
                    System.out.print("Veuillez saisir le numéro du livre pour obtenir plus d'informations : ");
                    String livreChoisi = this.scanner.nextLine().trim();

                    try {
                        int indexLivre = Integer.parseInt(livreChoisi) - 1; // -1 pour correspondre à l'index de la liste
                        if (indexLivre >= 0 && indexLivre < 5) {
                            indexLivre = page * 5 + indexLivre;
                            System.out.println(livres.get(indexLivre).completeDisplay() + "\n");
                        } 
                        else {
                            System.out.println("Veuillez entrer un numéro valide entre 1 et 5.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Veuillez entrer un numéro valide.");
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "d" -> {
                    this.changementLibClient();
                }
                case "e" -> {
                    this.menuConsultationPanier();
                }
                case "f" -> {
                    actif = false;
                }
                default -> {
                    if (choix.matches("[1-5]")) {
                        
                        Livre livreChoisi = livres.get(page * 5 + Integer.parseInt(choix) - 1); // pas d'exception car regex valide 

                        System.out.println("Combien de copies souhaitez-vous ajouter au panier pour le livre : " + livreChoisi.getTitre() + " ?");
                        String nombreCopies = this.scanner.nextLine().trim();

                        try {
                            Integer nbCopie = Integer.valueOf(nombreCopies);
                            if(Reseau.checkStock(livreChoisi, Reseau.getLibrairie(this.client.getLibrairie()), nbCopie)){
                                this.client.ajouterAuPanier(livreChoisi, this.client.getLibrairie(), nbCopie);
                            }
                        } 
                        catch (NumberFormatException e) {
                            System.out.println("Le livre choisi n'est pas présent en quantité demandée dans la librairie \nVeuillez retenter l'action");
                        }
                        catch (QuantiteInvalideException e){
                            System.out.println("La valeur entrée n'est pas une quantité valide, veuillez entrer un nombre non négatif");
                        }

                    }
                    else {
                        System.out.println("Veuillez entrer un numéro valide.");
                    }
                }
            }
        }
    }

    /**
     * permet de consulter les livres avec les options d'action de l'administrateur
     */
    public void menuConsultationBooksAdmin(){

        boolean actif = true;
        Integer libActive = null;
        int page = 0;
        
        while (actif) {
            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);

            if (libActive == null) {
                libActive = this.selectionLibAdmin();
            } 
            else {

                try {
                    List<Livre> livres = new ArrayList<>(Reseau.getLibrairie(libActive).consulterStock().keySet());
                    
                    Collections.sort(livres); // Tri des livres selon ISBN
                    
                    int pageMax = livres.size() / 5 == 0 ? livres.size() / 5 :  (int)Math.ceil(livres.size()/5);
                    
                    System.out.println("╔════════════════════════════════════════════════════════════════╗");
                    System.out.println("║                    CONSULTATION DES LIVRES                     ║");
                    System.out.println("╠════════════════════════════════════════════════════════════════╣ ");
                    
                    this.page(livres.subList(page * 5, Math.min((page + 1) * 5, livres.size())),libActive);
                    
                    String pageString = "  Page " + (page + 1) + " / " + (pageMax + 1);
                    System.out.println("║                                                                ║"); // espacement
                    System.out.println("║ " + pageString + " ".repeat(62 - pageString.length()) + " ║");
                    
                    System.out.println("╠════════════════════════════════════════════════════════════════╣");
                    
                    System.out.println("║  Veuillez choisir une action :                                 ║");
                    
                    if (page > 0) {
                        System.out.println("║  A. Page précédente                                            ║");
                    } else {
                        System.out.println("║  A. Page précédente (désactivée)                               ║");
                    }
                    
                    if (page < pageMax) {
                        System.out.println("║  B. Page suivante                                              ║");
                    } else {
                        System.out.println("║  B. Page suivante (désactivée)                                 ║");
                    }
                    System.out.println("║  C. Information supplémentaire livre                           ║");
                    System.out.println("║  D. Changer de librairie                                       ║");
                    System.out.println("║  E. Modifier les quantités                                     ║");
                    System.out.println("║  F. Ajouter un livre                                           ║");
                    System.out.println("║  G. Transférer un livre                                        ║");
                    System.out.println("║  Q. Retour au menu principal                                   ║");
                    System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
                    System.out.print("Votre choix : ");
                    
                    String choix = this.scanner.nextLine().trim().toLowerCase();
                    
                    switch (choix) {
                        case "a" -> {
                            if (page > 0) {
                                page--;
                            } else {
                                System.out.println("Vous êtes déjà à la première page.");
                            }
                        }
                        case "b" -> {
                            if (page < pageMax) {
                                page++;
                            } else {
                                System.out.println("Vous êtes déjà à la dernière page.");
                            }
                        }
                        case "c" -> {
                            System.out.print("Veuillez saisir le numéro du livre pour obtenir plus d'informations : ");
                            String livreChoisi = this.scanner.nextLine().trim();
                            
                            try {
                                int indexLivre = Integer.parseInt(livreChoisi) - 1; // -1 pour correspondre à l'index de la
                                // liste
                                if (indexLivre >= 0 && indexLivre < 5) {
                                    indexLivre = page * 5 + indexLivre;
                                    System.out.println(livres.get(indexLivre).completeDisplay() + "\n");
                                } else {
                                    System.out.println("Veuillez entrer un numéro valide entre 1 et 5.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Veuillez entrer un numéro valide.");
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        case "d" -> {
                            libActive = this.selectionLibAdmin();
                        }
                        case "e" -> {
                            System.out.print("Entrez le numéro du livre ('A' pour annuler l'action) : ");
                            String nb = this.scanner.nextLine().trim().toLowerCase();

                            switch(nb){
                                case "a" -> {
                                    // do nothing
                                }

                                default -> {
                                    try {
                                        int index = Integer.parseInt(nb) - 1;
                                        
                                        if (index >= 0 && index < 5) {
                                            Livre book = livres.get(page*5+ index);
                                            this.menuUpdateQte(book,libActive);
                                        } 
                                        else {
                                            System.out.println("Veuillez entrer un numéro valide entre 1 et 5.");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Veuillez entrer un numéro valide.");
                                    } catch (IndexOutOfBoundsException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            }
                        }
                        case "f" -> {
                            this.menuAjoutLivre();
                        }

                        case "g" -> {
                            System.out.print("Entrez le numéro du livre ('A' pour annuler l'action) : ");
                            String nb = this.scanner.nextLine().trim().toLowerCase();

                            switch(nb){
                                case "a" -> {
                                    // do nothing
                                }

                                default -> {
                                    try {
                                        int index = Integer.parseInt(nb) - 1;
                                        
                                        if (index >= 0 && index < 5) {
                                            Livre book = livres.get(page*5+ index);
                                            this.menuTransfert(book,libActive);
                                        } 
                                        else {
                                            System.out.println("Veuillez entrer un numéro valide entre 1 et 5.");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("Veuillez entrer un numéro valide.");
                                    } catch (IndexOutOfBoundsException e) {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            }
                        }

                        case "q" -> {
                            actif = false;
                        }
                    }
                } catch (LibraryNotFoundException e) {
                    System.out.println("Une erreur s'est produite");
                }
            }
        }
    }

    /**
     * permet au client de consulter son panier
     */
    public void menuConsultationPanier() {

        if (this.client.getPanier().getContenu().isEmpty()) {
            System.out.println("Votre panier est vide.");
            return; // fin de la méthode
        }

        boolean actif = true;

        int page = 0;
        Integer selectedLib = null;


        while (actif) { 
        
            Map<Integer, Map<Livre, Integer>> content = this.client.getPanier().getContenu();

            // tout les livres appartiennent a la même librairie → simplifier l'usage
            if (content.keySet().size() == 1) {
                selectedLib = content.keySet().iterator().next();   // une unique librairie inconnu donc pour faire simple, utilisation de l'itérateur et next()
                Map<Livre, Integer> livres = content.get(selectedLib); 
                List<Livre> toDisplayLivres = new ArrayList<>(livres.keySet());
                Collections.sort(toDisplayLivres);

                int pageMax = toDisplayLivres.size()/5 == 0 ? toDisplayLivres.size()/5 : (int)Math.ceil(toDisplayLivres.size()/5); // la dernière page est remplie ou non

                List<String> displayLivre = new ArrayList<>();
                for(Livre livre : toDisplayLivres) {

                    String display = livre + " - " + livres.get(livre) + " exemplaire(s)";
                    displayLivre.add(display);
                }

                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║                     CONSULTATION DU PANIER                     ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣ ");

                this.page(displayLivre.subList(page * 5, Math.min((page + 1) * 5, displayLivre.size())),null);
                System.out.println("║                                                                ║"); // espacement

                String pageString = "  Page " + (page + 1) + " / " + (pageMax + 1);
                System.out.println("║ " + pageString + " ".repeat(62 - pageString.length()) + " ║");

                System.out.println("╠════════════════════════════════════════════════════════════════╣ ");
                System.out.println("║  Veuillez choisir une action :                                 ║");

                System.out.println("║  Saisissez le numéro d'un livre pour le supprimer/reduire      ║");
                System.out.println("║  quantité du panier                                            ║");
                System.out.println("║                                                                ║");

                if (page > 0) {
                    System.out.println("║  A. Page précédente                                            ║");
                }
                else {
                    System.out.println("║  A. Page précédente (désactivée)                               ║");
                }
                if (page < pageMax) {
                    System.out.println("║  B. Page suivante                                              ║");
                }
                else {
                    System.out.println("║  B. Page suivante (désactivée)                                 ║");
                }
                System.out.println("║  C. Commander                                                  ║");
                System.out.println("║  D. Retour                                                     ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝");
                System.out.print("Votre choix : ");

                String choix = this.scanner.nextLine().trim().toLowerCase();

                switch (choix) {
                    case "a" -> {
                        if(page > 0){
                            page--;
                        }
                    } 

                    case "b" -> {
                        if(page < pageMax){
                            page++;
                        }
                    }

                    case "c" -> {
                        this.menuCommande();
                    } 

                    case "d" -> {
                        actif = false;
                    } 

                    default -> {
                        System.out.println("Le choix entrée n'est pas un choix valide");
                    }
                }
            }
            else{
                // pour éviter la ré-selection de la librairie a chaque action
                if(selectedLib == null){
                    int nbLib = this.client.getPanier().getContenu().size();

                    System.out.println("╔════════════════════════════════════════════════════════════════╗");
                    System.out.println("║                     CONSULTATION DU PANIER                     ║");
                    System.out.println("╠════════════════════════════════════════════════════════════════╣");
                    System.out.println("║  Veuillez choisir l'une des librairies                         ║");
                    System.out.println("║                                                                ║");

                    ArrayList<Librairie> toDisplay = new ArrayList<>();
                    for(Integer i : this.client.getPanier().getContenu().keySet()){
                        try {
                            toDisplay.add(Reseau.getLibrairie(i));
                        } 
                        catch (LibraryNotFoundException e) {
                        }
                    }

                    this.page(toDisplay,null);
                    System.out.println("║                                                                ║");
                    System.out.println("╠════════════════════════════════════════════════════════════════╣");
                    System.out.println("║  A. Revenir au menu principal                                  ║");
                    System.out.println("╚════════════════════════════════════════════════════════════════╝");
                    System.out.print("Votre choix : ");

                    String choix = this.scanner.nextLine().trim().toLowerCase();

                    switch (choix) {
                        case "a" -> {
                            actif = false;
                        }

                        default -> {
                            if (choix.matches("[1-" + nbLib + "]")) {

                                selectedLib = Integer.valueOf(choix);
                            } else {
                                System.out.println("Veuillez entrer un numéro valide.");
                                continue; // relance de l'affichage
                            }
                        }
                    }
                }

                Map<Livre, Integer> livres = content.get(selectedLib); 
                List<Livre> toDisplayLivres = new ArrayList<>();
                if(!(livres == null)){
                    toDisplayLivres.addAll(livres.keySet());
                }
                Collections.sort(toDisplayLivres);

                int pageMax = toDisplayLivres.size()/5 == 0 ? toDisplayLivres.size()/5 :  (int)Math.ceil(toDisplayLivres.size()/5); // la dernière page est remplie ou non

                List<String> displayLivre = new ArrayList<>();
                for(Livre livre : toDisplayLivres) {
                    Integer qte = livres.get(livre);
                    if(!(qte == null)){
                        String display = livre + " - " + qte + " exemplaire(s)";
                        displayLivre.add(display);
                    }
                }

                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║                     CONSULTATION DU PANIER                     ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣ ");

                this.page(displayLivre.subList(page * 5, Math.min((page + 1) * 5, displayLivre.size())),null);
                System.out.println("║                                                                ║"); // espacement

                String pageString = "  Page " + (page + 1) + " / " + (pageMax + 1);
                System.out.println("║ " + pageString + " ".repeat(62 - pageString.length()) + " ║");

                System.out.println("╠════════════════════════════════════════════════════════════════╣ ");
                System.out.println("║  Veuillez choisir une action :                                 ║");

                System.out.println("║  Saisissez le numéro d'un livre pour le supprimer/reduire      ║");
                System.out.println("║  quantité du panier                                            ║");
                System.out.println("║                                                                ║");

                if (page > 0) {
                    System.out.println("║  A. Page précédente                                            ║");
                }
                else {
                    System.out.println("║  A. Page précédente (désactivée)                               ║");
                }
                if (page < pageMax) {
                    System.out.println("║  B. Page suivante                                              ║");
                }
                else {
                    System.out.println("║  B. Page suivante (désactivée)                                 ║");
                }
                System.out.println("║  C. Commander                                                  ║");
                System.out.println("║  D. Retour                                                     ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝");
                System.out.print("Votre choix : ");

                String choix = this.scanner.nextLine().trim().toLowerCase();

                switch (choix) {
                    case "a" -> {
                        if(page > 0){
                            page--;
                        }
                    } 

                    case "b" -> {
                        if(page < pageMax){
                            page++;
                        }
                    }

                    case "c" -> {
                        
                        this.menuCommande();
                    } 

                    case "d" -> {
                        actif = false;
                    } 

                    default -> {
                        System.out.println("Le choix entrée n'est pas un choix valide");
                    }
                }
            }
        }
    }

    /**
     * permet au client de changer de librairie courrante
     */
    public void changementLibClient() {

        boolean actifLibrairie = true;

            while (actifLibrairie) {
                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║                       Choix de Librairie                       ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  Veuillez choisir votre librairie :                            ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");


                this.page(Reseau.librairies,null);

                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  A. annuler                                                    ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
                System.out.print("Votre choix : ");
                String choixLibrairie = this.scanner.nextLine().trim().toLowerCase();

                switch(choixLibrairie){

                    case "a" -> {
                        actifLibrairie = false;
                        this.menuPClient();
                    }

                    default -> {
                        int idLibrairie = Integer.parseInt(choixLibrairie);
                        if (idLibrairie >= 1 && idLibrairie <= Reseau.librairies.size()) {
                            actifLibrairie = false;
                            this.client.setLibrairie(idLibrairie);
                        } 
                        else {
                            System.out.println("Veuillez entrer un numéro de librairie valide.");
                        }
                    }
                }
            }
    }

    /**
     * permet de selectionner une librairie active pour l'admin
     * @return Integer : l'id de la librairie
     */
    public Integer selectionLibAdmin(){
        boolean actifLibrairie = true;
        Integer lib = null;

        while (actifLibrairie) {
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                       Choix de Librairie                       ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║  Veuillez choisir votre librairie :                            ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");

            this.page(Reseau.librairies, null);

            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║  A. annuler                                                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
            System.out.print("Votre choix : ");
            String choixLibrairie = this.scanner.nextLine().trim().toLowerCase();

            switch (choixLibrairie) {

                case "a" -> {
                    actifLibrairie = false;
                }

                default -> {
                    int idLibrairie = Integer.parseInt(choixLibrairie)-1;
                    if (idLibrairie >= 0 && idLibrairie < Reseau.librairies.size()) {
                        actifLibrairie = false;
                        lib = idLibrairie;
                    } else {
                        System.out.println("Veuillez entrer un numéro de librairie valide.");
                    }
                }
            }
        }
        return lib;
    }

    /**
     * permet de mettre a jour les données d'un client
     */
    public void menuUpdateInfoClient() {
        
        boolean actif = true;

        while(actif){

            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                   CHANGEMENT  D'INFORMATIONS                   ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║  Quelle information voulez-vous modifier                       ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║  A. Nom                                                        ║");
            System.out.println("║  B. Prénom                                                     ║");
            System.out.println("║  C. Mot de passe                                               ║");
            System.out.println("║  D. Adresse                                                    ║");
            System.out.println("║  E. Ville et code postal                                       ║");
            System.out.println("║  F. Annuler                                                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
            System.out.print("Votre choix : ");

            String choix = this.scanner.nextLine().trim().toLowerCase();

            switch(choix){
                
                case "a" -> {

                    System.out.println("Entrez votre nouveau nom (entrez 'A' pour annuler) : ");
                    String nom = this.scanner.nextLine().trim();

                    switch(nom){

                        case "a","A" -> {
                            // do nothing and wait the next menu display
                        }

                        default -> {
                            try {
                                this.client.modifInfo(EnumInfoClient.NOM, nom);    
                            } 
                            catch (SQLException e) {
                                System.out.println("une erreur est survenu lors du changement de nom");
                            }
                        }
                    }

                }
            
                case "b" -> {
                    System.out.println("Entrez votre nouveau Prénom (entrez 'A' pour annuler) : ");
                    String prenom = this.scanner.nextLine().trim();

                    switch(prenom){

                        case "a","A" -> {
                            // do nothing and wait the next menu display
                        }

                        default -> {
                            try {
                                this.client.modifInfo(EnumInfoClient.PRENOM, prenom);    
                            } 
                            catch (SQLException e) {
                                System.out.println("une erreur est survenu lors du changement de prenom");
                            }
                        }
                    }
                }
                
                case "c" -> {

                    System.out.print("Entrez votre ancien mot de passe (entrez 'A' pour annuler) : ");
                    String oldMdp = this.scanner.nextLine().trim();
                

                    switch(oldMdp){

                        case "a","A" -> {
                            // do nothing and wait the next menu display
                        }

                        default -> {
                            
                            System.out.print("Entrez votre nouveau mot de passe (entrez 'A' pour annuler) : ");
                            String newMdp = this.scanner.nextLine().trim();

                            switch (newMdp) {
                                case "a","A" -> {
                                    // do nothing
                                }
                                    
                                default -> {
                                    if (!oldMdp.equals(newMdp)) {
                                        try {
                                            this.client.modifInfo(EnumInfoClient.MDP, newMdp);
                                        } catch (SQLException e) {
                                            System.out.println("une erreur est survenu lors du changement de mot de passe");
                                        }
                                    }
                                    else{
                                        System.out.println("Votre nouveau de mot de passe n'est pas valide");
                                    }
                                }
                            }
                        }
                    }
                }

                case "d" -> {
                    System.out.println("Entrez votre nouvelle adresse (entrez 'A' pour annuler) : ");
                    String address = this.scanner.nextLine().trim();

                    switch(address){

                        case "a","A" -> {
                            // do nothing and wait the next menu display
                        }

                        default -> {
                            try {
                                this.client.modifInfo(EnumInfoClient.ADDRESS, address);    
                            } 
                            catch (SQLException e) {
                            }
                        }
                    }
                }

                case "e" -> {
                    System.out.println("Entrez votre nouvelle ville de résidence (entrez 'A' pour annuler) : ");
                    String ville = this.scanner.nextLine().trim();
                    System.out.println("Entrez votre nouveau code postal (entrez 'A' pour annuler) : ");
                    String codePostal = this.scanner.nextLine().trim();
                    

                    switch(ville){

                        case "a","A" -> {
                            // do nothing and wait the next menu display
                        }

                        default -> {

                            try {
                                this.client.modifInfo(EnumInfoClient.VILLE, ville);
                            } catch (SQLException e) {
                            }

                            switch (codePostal) {
                                case "a","A" -> {
                                    // do nothing
                                }

                                default -> {
                                    try {
                                        this.client.modifInfo(EnumInfoClient.CODEPOSTAL, codePostal);
                                    } catch (SQLException e) {
                                    }
                                }
                            }
                        }
                    }
                }

                case "f" -> {
                    actif = false;
                }
            }
        }
    }

    /**
     * menu de gestion de la réalisation d'une commande par le client
     */
    public void menuCommande() { 
        
        boolean actif = true;
        String modeLivraison = null;
        Boolean enLigne = null;
        Boolean facture = null; 

        while (actif) {

            if (modeLivraison == null) {
                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║                   PRÉPARATION DE LA COMMANDE                   ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  Comment voulez-vous vous faire livrer ?                       ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  A. En livraison a Domicile                                    ║");
                System.out.println("║  B. En Librairie                                               ║");
                System.out.println("║  C. Annuler                                                    ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
                System.out.print("Votre choix : ");

                String choix = this.scanner.nextLine().trim().toLowerCase();

                switch(choix){
                    case "a" -> {
                        modeLivraison = "C";
                    }

                    case"b" -> {
                        modeLivraison = "D";
                    }

                    case"c" -> {
                        actif = false;
                    }

                    default -> {
                        System.out.println("Ce choix n'est pas disponible.\nVeuillez renter.");
                    }
                }
            }
            else if(enLigne == null){
                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║                   PRÉPARATION DE LA COMMANDE                   ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  Où avez-vous fait la commande ?                               ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  A. En ligne                                                   ║");
                System.out.println("║  B. En Librairie                                               ║");
                System.out.println("║  C. Annuler                                                    ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
                System.out.print("Votre choix : ");

                String choix = this.scanner.nextLine().trim().toLowerCase();

                switch(choix){
                    case "a" -> {
                        enLigne = true;
                    }

                    case"b" -> {
                        enLigne = false;
                    }

                    case"c" -> {
                        actif = false;
                    }

                    default -> {
                        System.out.println("Ce choix n'est pas disponible.\nVeuillez renter.");
                    }
                }
            }
            else if (facture == null) {
                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║                   PRÉPARATION DE LA COMMANDE                   ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  Voulez vous imprimer un facture ? (Y/N)                       ║");
                System.out.println("║  Entrez 'C' pour annuler                                       ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
                System.out.print("Votre choix : ");

                String choix = this.scanner.nextLine().trim().toLowerCase();

                switch(choix){
                    case "y" -> {
                        facture = true;
                    }

                    case"n" -> {
                        facture = false;
                    }

                    case"c" -> {
                        actif = false;
                    }

                    default -> {
                        System.out.println("Ce choix n'est pas disponible.\nVeuillez renter.");
                    }
                }
            }
            else{
                if(this.client.commander(modeLivraison, enLigne, facture)){
                    System.out.println("La commande a bien été effectué.");
                }
                else{
                    System.out.println("La commande a bien été effectué.");
                }
                actif = false;
            }
        }
    }

    /**
     * menu de gestion des recommandations
     */
    public void menuRecommandation() {

        boolean actif = true;
        Integer nbRecommandation = null;
        int page = 0;

        while (nbRecommandation == null && actif) {

                System.out.println("Veuillez entrer le nombre maximal de recommandation souhaité \n( Entrez A pour annuler et revenir au menu principal )");
                String value = this.scanner.nextLine().trim().toLowerCase();

                switch (value) {

                    case "a" -> {
                        actif = false;
                    }

                    default -> {
                        try {
                            nbRecommandation = Integer.valueOf(value);
                            if (nbRecommandation < 1) {
                                throw new NumberFormatException(); // aller dans le catch
                            }
                        } catch (NumberFormatException e) {
                            actif = true; // ré-afficher le menu
                            System.out.println("Votre choix n'est pas un chiffre valide");
                        }
                    }
                }
            }

        while (actif) {
                try {

                List<Livre> recommandations = this.client.OnVousRecommande(nbRecommandation);
                int pageMax = recommandations.size() / 5 == 0 ? (int) recommandations.size()/ 5:  (int)Math.ceil(recommandations.size()/5);

                if(recommandations.isEmpty()){
                    System.out.println("╔════════════════════════════════════════════════════════════════╗");
                    System.out.println("║                         RECOMMANDATION                         ║");
                    System.out.println("╠════════════════════════════════════════════════════════════════╣");
                    System.out.println("║                                                                ║");
                    System.out.println("║  Nous n'avons aucune recommandation de livre a vous faire      ║");
                    System.out.println("║  pour le moment                                                ║");
                    System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
                    actif = false;
                } 
                else {

                    System.out.println("╔════════════════════════════════════════════════════════════════╗");
                    System.out.println("║                         RECOMMANDATION                         ║");
                    System.out.println("╠════════════════════════════════════════════════════════════════╣");
                    System.out.println("║                                                                ║");

                    this.page(recommandations.subList(page * 5, Math.min((page + 1) * 5, recommandations.size())),
                            this.client.getLibrairie());
                    System.out.println("║                                                                ║");
                    String pageString = "  Page " + (page + 1) + " / " + (pageMax + 1);
                    System.out.println("║ " + pageString + " ".repeat(62 - pageString.length()) + " ║");
                    System.out.println("╠════════════════════════════════════════════════════════════════╣");
                    System.out.println("║  Saisissez le numéro d'un livre pour l'ajouter au panier       ║");
                    System.out.println("║  Veuillez choisir une action :                                 ║");
                    System.out.println("║                                                                ║");
                    if (page > 0) {
                        System.out.println("║  A. Page précédente                                            ║");
                    } else {
                        System.out.println("║  A. Page précédente (désactivée)                               ║");
                    }
                    if (page < pageMax) {
                        System.out.println("║  B. Page suivante                                              ║");
                    } else {
                        System.out.println("║  B. Page suivante (désactivée)                                 ║");
                    }

                    System.out.println("║  C. Information supplémentaire livre                           ║");
                    System.out.println("║  D. Retour au menu principal                                   ║");
                    System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
                    System.out.println("Votre choix :");

                    String choix = this.scanner.nextLine().trim().toLowerCase();

                    switch (choix) {

                        case "a" -> {
                            if (page > 0) {
                                page--;
                            }
                        }

                        case "b" -> {
                            if (page < pageMax) {
                                page++;
                            }
                        }

                        case "c" -> {
                            System.out.print("Veuillez saisir le numéro du livre pour obtenir plus d'informations : ");
                            String livreChoisi = this.scanner.nextLine().trim();

                            try {
                                int indexLivre = Integer.parseInt(livreChoisi) - 1; // -1 pour correspondre à l'index de
                                                                                    // la liste
                                if (indexLivre >= 0 && indexLivre < 5) {
                                    indexLivre = page * 5 + indexLivre;
                                    System.out.println(recommandations.get(indexLivre).completeDisplay() + "\n");
                                } else {
                                    System.out.println("Veuillez entrer un numéro valide entre 1 et 5.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Veuillez entrer un numéro valide.");
                            } catch (IndexOutOfBoundsException e) {
                                System.out.println(e.getMessage());
                            }
                        }

                        case "d" -> {
                            actif = false;
                        }

                        default -> {
                            if (choix.matches("[1-5]")) {

                                Livre livreChoisi = recommandations.get(page * 5 + Integer.parseInt(choix) - 1); // pas
                                                                                                                 // d'exception
                                                                                                                 // en
                                                                                                                 // théorie
                                                                                                                 // car
                                                                                                                 // regex
                                                                                                                 // valide

                                System.out.println("Combien de copies souhaitez-vous ajouter au panier pour le livre : "
                                        + livreChoisi.getTitre() + " ?");
                                String nombreCopies = this.scanner.nextLine().trim();

                                try {
                                    Integer nbCopie = Integer.valueOf(nombreCopies);
                                    if (Reseau.checkStock(livreChoisi, Reseau.getLibrairie(this.client.getLibrairie()),
                                            nbCopie)) {
                                        this.client.ajouterAuPanier(livreChoisi, this.client.getLibrairie(), nbCopie);
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println(
                                            "Le livre choisi n'est pas présent en quantité demandée dans la librairie \nVeuillez retenter l'action");
                                } catch (QuantiteInvalideException e) {
                                    System.out.println(
                                            "La valeur entrée n'est pas une quantité valide, veuillez entrer un nombre positif");
                                } catch (LibraryNotFoundException e) {
                                    System.out.println("Une erreur est survenu");
                                    actif = false;
                                }
                            }
                        }
                    }
                }
            } catch (LibraryNotFoundException e) {
                System.out.println("Une erreur est survenu");
                actif = false;
            }
        }
    }

    /**
     * menu de gestion de l'ajout d'un nouveau livre
     */
    public void menuAjoutLivre(){

        boolean actif = true;
        String isbn = null;
        String titre = null;
        int nbPages = 0;
        int dtePubli = 0;
        double prix = 0;
        String classification = null;
        String editeur = null;
        List<Auteur> auteurs = null;
        Integer librairie = null;
        int qte = 0;

        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                        Ajouter un livre                        ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
        System.out.println("A chaque instant, entrez 'A' pour annuler et revenir au menu principal");
        System.out.println("Veuillez entrer les informations suivantes concernant le livre : \n");

        System.out.print("Entez l'ISBN du livre : ");
        isbn = this.scanner.nextLine().trim();

        switch (isbn){
            case "a","A" -> {
                actif = false;
            }
        }

        if(actif){
            System.out.print("Entez le titre du livre : ");
            titre = this.scanner.nextLine().trim();

            switch (titre){
                case "a","A" -> {
                    actif = false;
                }
            }
        }

        if (actif) {
            System.out.print("Entrez le nombre de pages du livre : ");
            String nb = this.scanner.nextLine().trim();
            switch (nb) {
                case "a", "A" -> {
                    actif = false;
                }

                default -> {
                    try {
                        nbPages = Integer.parseInt(nb);
                    } catch (NumberFormatException | NullPointerException e) {
                        System.out.println("L'entée est invalide, annulation de l'ajout");
                        actif = false;
                    }
                }
            }
        }

        if(actif){
            System.out.print("Entrez l'année de publication : ");
            String annee = this.scanner.nextLine().trim(); 
            switch (annee) {
                case "a", "A" -> {
                    actif = false;
                }

                default -> {
                    try {
                        dtePubli = Integer.parseInt(annee);
                    } catch (NumberFormatException | NullPointerException e) {
                        System.out.println("L'entée est invalide, annulation de l'ajout");
                        actif = false;
                    }
                }
            }
        }

        if(actif){
            System.out.print("Entrez le prix du livre : ");
            String prixS = this.scanner.nextLine().trim(); 
            switch (prixS) {
                case "a", "A" -> {
                    actif = false;
                }

                default -> {
                    try {
                        prix = Double.parseDouble(prixS);
                    } catch (NumberFormatException | NullPointerException e ) {
                        System.out.println("L'entée est invalide, annulation de l'ajout");
                        actif = false;
                    }
                }
            }
        }

        if(actif){
            System.out.print("Entez la classification du livre : ");
            classification = this.scanner.nextLine().trim();

            switch (classification){
                case "a","A" -> {
                    actif = false;
                }
            }
        }

        if(actif){
            System.out.print("Entez le nom de l'éditeur du livre : ");
            editeur = this.scanner.nextLine().trim();

            switch (editeur){
                case "a","A" -> {
                    actif = false;
                }
            }
        }

        if(actif){
            auteurs = this.selectAuteur();

            // si annulé dans le menu
            if(auteurs == null) actif = false;
        }

        if(actif){
            librairie = this.selectionLibAdmin();

            if(librairie == null) actif = false;
        }

        if(actif){
            System.out.print("Entrez la quantité de livre a mettre en stock : ");
            String qteS = this.scanner.nextLine().trim(); 
            switch (qteS) {
                case "a", "A" -> {
                    actif = false;
                }

                default -> {
                    try {
                        qte = Integer.parseInt(qteS);
                    } catch (NumberFormatException | NullPointerException e ) {
                        System.out.println("L'entée est invalide, annulation de l'ajout");
                        actif = false;
                    }
                }
            }            
        }

        if(actif){
            try {
                Livre toAdd = new Livre(isbn, titre, editeur, dtePubli, prix, nbPages, classification,null);
                Librairie lib = Reseau.getLibrairie(librairie);
                lib.ajouterNouveauLivre(toAdd, qte);
                System.out.println("Le livre a été ajouté !");
            } 
            catch (LibraryNotFoundException | QuantiteInvalideException | SQLException e) {
                System.out.println("Une erreur est survenu");
            }
        }

    }

    /**
     * permet de selectionner les auteurs lors de l'ajout d'un livre
     * @return
     */
    public List<Auteur> selectAuteur(){

        boolean actif = true;
        List<Auteur> auteurs = new ArrayList<>();

        try {
    
            List<Auteur> allAuthors = Reseau.getAllAuthors();
            int page = 0;
            int pageMax = allAuthors.size() % 5 == 0 ? (int) allAuthors.size() / 5 :  (int)Math.ceil(allAuthors.size()/5);

            while (actif) {
                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║                      SELECTION DES AUTEURS                     ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║   Entrez le numéro d'un auteur pour l'ajouter a la liste       ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║                                                                ║");

                this.page(allAuthors.subList(page * 5, Math.min((page + 1) * 5, allAuthors.size())), null);

                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║                                                                ║");

                this.page(auteurs, null);

                System.out.println("║                                                                ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║                                                                ║");

                System.out.println("║  A. Annuler                                                    ║");
                if (page > 0) {
                    System.out.println("║  B. Page précédente                                            ║");
                } else {
                    System.out.println("║  B. Page précédente (désactivée)                               ║");
                }

                if (page < pageMax) {
                    System.out.println("║  C. Page suivante                                              ║");
                } else {
                    System.out.println("║  C. Page suivante (désactivée)                                 ║");
                }
                System.out.println("║  D. Selection terminé                                          ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");

                String choix = this.scanner.nextLine().trim().toLowerCase();

                switch (choix) {
                    case "a" -> {
                        actif = false;
                        auteurs = null;
                    }
                    case "b" -> {
                        if(page > 0){
                            page --;
                        }
                    }
                    case "c" -> {
                        if(page < pageMax ){
                            page++;
                        }
                    }
                    case "d" -> {
                        actif = false;
                    }

                    default -> {

                        try {
                            int index = Integer.parseInt(choix) - 1;
                            if (index >= 0 && index < 5) {
                                auteurs.add(allAuthors.get(page*5 + index));
                            } else {
                                System.out.println("Veuillez entrer un numéro valide entre 1 et 5.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Veuillez entrer un nombre valide.");
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Une erreur est survenu");
        }
        return auteurs;
    }

    /**
     * permet a l'admin de mettre a jour la quantité d'un livre d'un librairie
     * @param livre
     * @param librairieActive
     */
    public void menuUpdateQte(Livre livre,int librairieActive){
        System.out.println("Veuillez entrer la quantité de livre a retirer (quantité négative) ou a ajouter (quantité positive), entrez 'A pour annuler");
        System.out.print("Quantité : ");
        String qteS = this.scanner.nextLine().trim().toLowerCase();

        switch (qteS) {
            case "a" -> {
                // do nothing
            }

            default -> {
                try {
                    int qte = Integer.parseInt(qteS);
                    Librairie lib = Reseau.getLibrairie(librairieActive);
                    if(qte < 0){
                        lib.retirerLivre(livre, -qte);
                    }
                    else if(qte > 0){
                        lib.ajouterNouveauLivre(livre, qte);
                    }
                } 
                catch (NumberFormatException | NullPointerException e) {
                    System.out.println("La valeur entrée n'est pas un nombre");
                }
                catch (LibraryNotFoundException | QuantiteInvalideException | BookNotInStockException | SQLException e) {
                    System.out.println("Une erreur est survenu, l'action est annulé");
                }
            }
        }
    }

    /**
     * permet le transfert d'un livre d'une librairie a une autre
     * @param livre
     * @param originLib
     */
    public void menuTransfert(Livre livre,Integer originLib){
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           Transfert                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");

        Integer qte = null;
        Integer idLib = null;

        System.out.print("Veuillez enter le nombre de livre a transférer ('A' pour annuler) : ");
        String qteS = this.scanner.nextLine().trim().toLowerCase();

        switch(qteS){
            case "a" -> {
                // laisse qte a null
            }
            default -> {
                try {
                    qte = Integer.valueOf(qteS);
                } catch (NumberFormatException | NullPointerException e) {
                    System.err.println("La valeur entrée n'est pas une valeur numérique, abandons du transfert");
                }
            }
        }

        if(!(qte==null)){
            idLib = this.selectionLibAdmin();
        }

        if(!(idLib == null || originLib == null) && qte != null){

            try {
                Reseau.transfert(livre,qte,originLib,idLib);
            } 
            catch (BookNotInStockException | LibraryNotFoundException | SQLException e) {
                System.out.println("Une erreur est survenu lors du transfert");
            }
            catch (QuantiteInvalideException e){
                System.out.println("La quantité indiquée est invalide");
            }
        }
    }

    /**
     * permet a l'admin, la consultation des statistiques de la chaine de librairie
     */
    public void menuStats(){

        boolean actif = true;

        while(actif){
                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║                          Statistiques                          ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  Palmarès                                                      ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  A. Auteurs                                                    ║");
                System.out.println("║  B. Livres                                                     ║");
                System.out.println("║  C. Librairies                                                 ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  Chiffre d'affaire                                             ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  D. CA total                                                   ║");
                System.out.println("║  E. CA par librairie                                           ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  F. annuler                                                    ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
                System.out.print("Votre choix : ");
                String choix = this.scanner.nextLine().trim().toLowerCase();

                switch(choix){

                    case "a" -> {
                        try {
                            Map<Auteur, Integer> palmares = Reseau.getPalmares(10, EnumPalmares.AUTEUR);
                            int i = 1;
                            System.out.println("╔════════════════════════════════════════════════════════════════╗");
                            System.out.println("║                        Palmarès Auteurs                        ║");
                            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");

                            for(Auteur auteur : palmares.keySet()){
                                System.out.println(i + ". " + auteur + " avec un nombre de vente de " + palmares.get(auteur));
                                i++;
                            }
                            
                            System.out.println("\nAppuyer sur une touche pour revenir au menu");
                            this.scanner.nextLine();
                        } 
                        catch (SQLException e) {
                            System.out.println("Une erreur est survenu");
                        }
                    }

                    case "b" -> {
                        try {
                            Map<Livre, Integer> palmares = Reseau.getPalmares(10, EnumPalmares.LIVRE);
                            int i = 1;
                            System.out.println("╔════════════════════════════════════════════════════════════════╗");
                            System.out.println("║                         Palmarès Livre                         ║");
                            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");

                            for(Livre book : palmares.keySet()){
                                System.out.println(i + ". " + book + " avec un nombre total de commande de " + palmares.get(book));
                                i++;
                            }
                            
                            System.out.println("\nAppuyer sur une touche pour revenir au menu");
                            this.scanner.nextLine();
                        } 
                        catch (SQLException e) {
                            System.out.println("Une erreur est survenu");
                        }
                    }

                    case "c" -> {
                        try {
                            Map<Librairie, Integer> palmares = Reseau.getPalmares(10, EnumPalmares.LIBRAIRIE);
                            int i = 1;
                            System.out.println("╔════════════════════════════════════════════════════════════════╗");
                            System.out.println("║                        Palmarès Auteurs                        ║");
                            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");

                            for(Librairie lib : palmares.keySet()){
                                System.out.println(i + ". " + lib + "  avec un total de commande de  " + palmares.get(lib));
                                i++;
                            }
                            
                            System.out.println("\nAppuyer sur une touche pour revenir au menu");
                            this.scanner.nextLine();
                        } 
                        catch (SQLException e) {
                            System.out.println("Une erreur est survenu");
                        }
                    }

                    case "d" -> {
                        System.out.println("╔════════════════════════════════════════════════════════════════╗");
                        System.out.println("║                    Chiffre d'affaire total                     ║");
                        System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
                        Map<Librairie,Double> CA = Reseau.CAByLibrairie();
                        Double sum = 0.0;
                        for(Double nb : CA.values()){sum += nb;}

                        System.out.println("Total : " + sum +" €");
                        System.out.println("\nAppuyer sur une touche pour revenir au menu");
                        this.scanner.nextLine();                        
                    }

                    case "e" -> {
                        System.out.println("╔════════════════════════════════════════════════════════════════╗");
                        System.out.println("║                 Chiffre d'affaire par librairie                ║");
                        System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");

                        Map<Librairie,Double> CA = Reseau.CAByLibrairie();

                        for(Librairie lib : CA.keySet()){
                            System.out.println(lib + "  " + CA.get(lib) + " €");
                        }

                        System.out.println("\nAppuyer sur une touche pour revenir au menu");
                        this.scanner.nextLine(); 
                    }

                    case "f" -> {actif = false;}

                }
        }


    }

    /**
     * permet la déconnexion de l'utilisateur actif
     */
    public void deconnexion() {

        System.out.println("Déconnexion réussie.");

        this.client = null;
        //this.vendeur = null;

        // Retour au menu d'identification
        this.menuIdentification();
    }

    /**
     * permet de quitter l'application
     */
    public void quitter() {

        System.out.println("Merci d'avoir utilisé l'application.\nAu revoir !");

        this.scanner.close();
        System.exit(0);
    }

    /**
     * permet l'affichage d'une page d'éléments tel que des livres, des auteurs et librairies
     * 
     * @param <T> le type de l'élément affiché
     * @param data la liste des données contenu dans la "Page"
     */
    private <T> void page(List<T> data,Integer librairie){

        Librairie lib = null;

        if(librairie != null){
            try {
                lib = Reseau.getLibrairie(librairie);
            } 
            catch (LibraryNotFoundException e) {
                System.out.println("Une erreur est parvenu lors de la création d'un page pour la selection de la librairie");
            }
        }

        int index = 1;

        for (T toDisplay : data) {

            String toDisplayString = "  " + index + "." + toDisplay.toString();
            if(toDisplayString.length() > 62) {
                toDisplayString = toDisplayString.substring(0, 59) + "...";
            }
            else{
                toDisplayString = toDisplayString + " ".repeat(62 - toDisplayString.length()); // Ajustement de la longueur pour l'affichage
            }

            // afficher les stocks si c'est un livre
            if(toDisplay instanceof Livre && lib != null){
                String qte = lib.consulterStock().get((Livre)toDisplay)+"";
                toDisplayString = toDisplayString.substring(0, toDisplayString.length() - (qte.length() + 1)) + " " + qte;
            }   

            System.out.println("║ " + toDisplayString + " ║");
            index++;
        }
    }

    public static void main(String[] args) {

        ApplicationTerminal app = new ApplicationTerminal();
        app.menuIdentification();
    }
}

