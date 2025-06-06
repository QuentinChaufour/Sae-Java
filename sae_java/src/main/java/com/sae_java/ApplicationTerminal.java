package com.sae_java;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
                    actif = false;
                    menuIdentificationVendeur();
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
            this.client = Reseau.identificationClient(nom, prenom, motDePasse, 0);

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

    public void menuIdentificationVendeur() {
        System.out.println("== Identification Vendeur ==");
    }

    public void menuIdentificationAdmin() {
        System.out.println("== Identification Administrateur ==");
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
            System.out.println("║  E. Déconnexion                                                ║");
            System.out.println("║  F. Quitter                                                    ║");
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
                    actif = false;
                    this.deconnexion();
                }
                case "f" -> {
                    actif = false;
                    this.quitter();
                }
                default -> System.out.println("Choix invalide. \n Veuillez réessayer.");
            }
        }
        

    }

    public void menuConsultationBooks() throws LibraryNotFoundException{

        int page = 0; // page de base 
        boolean actif = true;

        while (actif) {

            List<Livre> livres = new ArrayList<>(Reseau.getLibrairie(this.client.getLibrairie()).consulterStock().keySet()); // en liste pour pouvoir trier les livres
            Collections.sort(livres); // Tri des livres selon ISBN

            int pageMax = livres.size()/5 == 0 ? livres.size()/5 : ((int)(livres.size()/5)) + 1; // la dernière page est remplie ou non 

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

            if (page < pageMax - 1) {
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
                    if (page < pageMax - 1) {
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
                        if (indexLivre >= 0 && indexLivre <= 5) {
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

    public void menuConsultationPanier() {

        if (this.client.getPanier().getContenu().isEmpty()) {
            System.out.println("Votre panier est vide.");
            return; // fin de la méthode
        }

        boolean actif = true;

        Map<Integer, Map<Livre, Integer>> content = this.client.getPanier().getContenu();
        int page = 0;
        Integer selectedLib = null;


        while (actif) { 
        
            // tout les livres appartiennent a la même librairie → simplifier l'usage
            if (content.keySet().size() == 1) {
                selectedLib = content.keySet().iterator().next();   // une unique librairie inconnu donc pour faire simple, utilisation de l'itérateur et next()
                Map<Livre, Integer> livres = content.get(selectedLib); 
                List<Livre> toDisplayLivres = new ArrayList<>(livres.keySet());
                Collections.sort(toDisplayLivres);

                int pageMax = toDisplayLivres.size()/5 == 0 ? toDisplayLivres.size()/5 : ((int)(toDisplayLivres.size()/5)) + 1; // la dernière page est remplie ou non

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
                if (page < pageMax - 1) {
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
                List<Livre> toDisplayLivres = new ArrayList<>(livres.keySet());
                Collections.sort(toDisplayLivres);

                int pageMax = toDisplayLivres.size()/5 == 0 ? toDisplayLivres.size()/5 : ((int)(toDisplayLivres.size()/5)) + 1; // la dernière page est remplie ou non

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
                if (page < pageMax - 1) {
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
                            this.client.setLibrairie(idLibrairie - 1); // 1 de diff avec l'affichage
                        } 
                        else {
                            System.out.println("Veuillez entrer un numéro de librairie valide.");
                        }
                    }
                }
            }
    }

    public void menuUpdateInfoClient() {
        System.out.println("== Mise à jour des infos client ==");
    }

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

    public void menuRecommandation() {

        boolean actif = true;
        Integer nbRecommandation = null;
        int page = 0;

        while (nbRecommandation == null && actif) {

                System.out.println(
                        "Veuillez entrer le nombre maximal de recommandation souhaité \n( Entrez A pour annuler et revenir au menu principal )");
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
                int pageMax = recommandations.size() / 5 == 0 ? (int) recommandations.size() / 5: (int) (recommandations.size() / 5) + 1;

                System.out.println("╔════════════════════════════════════════════════════════════════╗");
                System.out.println("║                         RECOMMANDATION                         ║");
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║                                                                ║");

                this.page(recommandations.subList(page * 5, Math.min((page + 1) * 5, recommandations.size())), this.client.getLibrairie());
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
                if (page < pageMax - 1) {
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
                        if (page < pageMax - 1) {
                            page++;
                        }
                    }

                    case "c" -> {
                        System.out.print("Veuillez saisir le numéro du livre pour obtenir plus d'informations : ");
                        String livreChoisi = this.scanner.nextLine().trim();

                        try {
                            int indexLivre = Integer.parseInt(livreChoisi) - 1; // -1 pour correspondre à l'index de
                                                                                // la liste
                            if (indexLivre >= 0 && indexLivre <= 5) {
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

                            Livre livreChoisi = recommandations.get(page * 5 + Integer.parseInt(choix) - 1); // pas d'exception en théorie car regex valide

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
                            }
                        }
                    }
                }
            } catch (LibraryNotFoundException e) {
                System.out.println("Une erreur est survenu");
            }
        }
    }

    public void deconnexion() {

        System.out.println("Déconnexion réussie.");

        this.client = null;
        this.vendeur = null;

        // Retour au menu d'identification
        this.menuIdentification();
    }

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

        try{
            Reseau.createStatement("delete from testDETAILCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testPOSSEDER").executeUpdate();
            Reseau.createStatement("delete from testECRIRE").executeUpdate();
            Reseau.createStatement("delete from testAUTEUR").executeUpdate();
            Reseau.createStatement("delete from testLIVRE").executeUpdate();
            Reseau.createStatement("delete from testMAGASIN").executeUpdate();
            Reseau.createStatement("delete from testCLIENT").executeUpdate();

            Reseau.createStatement("insert into testMAGASIN values (0,'Librairie de la Fac','Orleans')").executeUpdate();
            Reseau.createStatement("insert into testMAGASIN values (1,'La librairie du centre','Tours')").executeUpdate();
            
            Reseau.createStatement("insert into testCLIENT values (3,'Eboue','Fabrice','Tient!!!','60 avenue de la Republique','75000','Paris')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (2,'Dupont','Jean','b','456 avenue de la Republique','75000','Paris')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (1,'Martin','Julie','c','133 boulevard de l universite','45000','Orleans')").executeUpdate();

            Reseau.createStatement("insert into testAUTEUR values (1,'H.G Wells',null,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (2,'Antoine de Saint-Exupéry',null,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (3,'Jim Davis',1945,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (4,'Tui T. Sutherland',1978,null)").executeUpdate();
            
            Reseau.createStatement("insert into testLIVRE values ('120','La Guerre des mondes',159,1898,9.99,'Science Fiction','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('121','Le Petit Prince',96,1943,7.99,'Roman','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('122','Harry Potter',96,1943,7.99,'Roman','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('123','Hunger Games',96,1943,7.99,'Science Fiction','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('124','Garfiel & Cie',96,1943,7.99,'BD','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('125','La Guerre des Clans',96,1943,7.99,'Roman','PKJ',null)").executeUpdate();
            
            Reseau.createStatement("insert into testECRIRE values ('120',1)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('121',2)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('124',3)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('125',4)").executeUpdate();

            Reseau.createStatement("insert into testPOSSEDER values (0,'120',4)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'121',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'122',2)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'124',1)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'125',9)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'120',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'121',7)").executeUpdate();

            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);
        }
        catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
            return;
        }


        ApplicationTerminal app = new ApplicationTerminal();
        app.menuIdentification();

        try {
            Reseau.createStatement("delete from testDETAILCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testPOSSEDER").executeUpdate();
            Reseau.createStatement("delete from testECRIRE").executeUpdate();
            Reseau.createStatement("delete from testAUTEUR").executeUpdate();
            Reseau.createStatement("delete from testLIVRE").executeUpdate();
            Reseau.createStatement("delete from testMAGASIN").executeUpdate();
            Reseau.createStatement("delete from testCLIENT").executeUpdate();
        } 
        catch (SQLException e) {
        }
    }
}

