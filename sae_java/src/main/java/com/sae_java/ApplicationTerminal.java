package com.sae_java;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
            System.out.println("║  1. Client                                                     ║");
            System.out.println("║  2. Vendeur                                                    ║");
            System.out.println("║  3. Administrateur                                             ║");
            System.out.println("║  4. Quitter                                                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
            System.out.print("Votre choix : ");

            String choix = this.scanner.nextLine();
            switch (choix) {
                case "1" -> {
                    actif = false;
                    this.menuIdentificationClient();
                }
                case "2" -> {
                    actif = false;
                    menuIdentificationVendeur();
                }
                case "3" -> {
                    actif = false;
                    menuIdentificationAdmin();
                }
                case "4" -> {
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
        System.out.println(" Veuillez entrer vos identifiants : \n");
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

                for (Librairie librairie : Reseau.librairies) {

                    String libraryString =  " " + (librairie.getId() + 1) + ". " + librairie.getNom() + " - " + librairie.getVille();

                    if (libraryString.length() > 62) {
                        libraryString = libraryString.substring(0, 59) + "...";
                    } else {
                        libraryString = libraryString + " ".repeat(62 - libraryString.length()); 
                    }
                    System.out.println("║ " + libraryString + " ║");
                }
                System.out.println("╠════════════════════════════════════════════════════════════════╣");
                System.out.println("║  0. Retour au menu principal                                   ║");
                System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
                System.out.print("Votre choix : ");
                String choixLibrairie = this.scanner.nextLine().trim();

                int idLibrairie = Integer.parseInt(choixLibrairie);
                if (idLibrairie >= 1 && idLibrairie <= Reseau.librairies.size()) {
                    actifLibrairie = false;
                    client.setLibrairie(idLibrairie - 1); // 1 de diff avec l'affichage
                }
                else if (idLibrairie == 0) {
                    actifLibrairie = false;
                    this.menuIdentification();
                } 
                else {
                    System.out.println("Veuillez entrer un numéro de librairie valide.");
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


    public void menuPClient() {

        boolean actif = true;

        while(actif){
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                         MENU PRINCIPAL                         ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║   Que voulez vous faire :                                      ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣");
            System.out.println("║  1. Consulter catalogue                                        ║");
            System.out.println("║  2. Changer librairie                                          ║");
            System.out.println("║  3. Consulter Panier                                           ║");
            System.out.println("║  4. Déconnexion                                                ║");
            System.out.println("║  5. Quitter                                                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");

            System.out.print("Votre choix : ");

            String choix = this.scanner.nextLine().trim();

            switch (choix) {
                case "1" -> {
                    actif = false;
                    try {
                        this.menuConsultationBooks();
                    } catch (LibraryNotFoundException e) {
                        System.out.println("Erreur : " + e.getMessage());
                        actif = true;
                    }
                }
                case "2" -> {
                    actif = false;
                    this.changementLibClient();
                }
                case "3" -> {
                    actif = false;
                    this.menuConsultationPanier();
                }
                case "4" -> {
                    actif = false;
                    this.deconnexion();
                }
                case "5" -> {
                    actif = false;
                    this.quitter();
                }
                default -> System.out.println("Choix invalide. \n Veuillez réessayer.");
            }
        }
        

    }

    public void menuPVendeur() {
        System.out.println("== Menu Vendeur ==");
    }

    public void menuConsultationBooks() throws LibraryNotFoundException{

        int page = 0; // page de base 
        boolean actif = true;
        
        List<Livre> livres = new ArrayList<>(Reseau.getLibrairie(this.client.getLibrairie()).consulterStock().keySet()); // en liste pour pouvoir trier les livres
        Collections.sort(livres); // Tri des livres selon ISBN

        int pageMax = livres.size()/5 == 0 ? livres.size()/5 : ((int)(livres.size()/5)) + 1; // la dernière page est remplie ou non 

        while (actif) {
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║                    CONSULTATION DES LIVRES                     ║");
            System.out.println("╠════════════════════════════════════════════════════════════════╣ ");

            this.page(livres.subList(page * 5, Math.min((page + 1) * 5, livres.size())));

            String pageString = "  Page " + (page + 1) + " / " + (pageMax+1);
            System.out.println("║                                                                ║"); // espacement
            System.out.println("║ " + pageString + " ".repeat(62 - pageString.length()) + " ║");

            System.out.println("╠════════════════════════════════════════════════════════════════╣");

            System.out.println("║  Veuillez choisir une action :                                 ║");

            System.out.println("║  Saisissez le numéro d'un livre pour l'ajouter au panier       ║");

            if( page > 0) {
                System.out.println("║  6. Page précédente                                            ║");
            } 
            //else {
            //    System.out.println("║ 1. Page précédente (désactivée)                               ║");
            //}

            if (page < pageMax - 1) {
                System.out.println("║  7. Page suivante                                               ║");
            }
                        //else {
            //    System.out.println("║ 1. Page suivante (désactivée)                                  ║");
            //}


            System.out.println("║  8. Consulter le panier                                        ║");

            System.out.println("║  9. Retour au menu principal                                   ║");


            System.out.println("╚════════════════════════════════════════════════════════════════╝ \n");
            
            String choix = this.scanner.nextLine().trim();

            switch (choix) {
                case "6" -> {
                    if (page > 0) {
                        page--;
                    }
                }
                case "7" -> {
                    if (page < pageMax - 1) {
                        page++;
                    }
                }
                case "8" -> {
                    actif = false;
                    this.menuPClient();
                }
                case "9" -> {
                    actif = false;
                    this.menuPClient();
                }
                default -> {
                    if (choix.matches("[1-5]")) {
                        
                        Livre livreChoisi = livres.get(page * 5 + Integer.parseInt(choix) - 1); // pas d'exception car regex valide 

                        System.out.println("Combien de copies souhaitez-vous ajouter au panier pour le livre : " + livreChoisi.getTitre() + " ?");
                        String nombreCopies = this.scanner.nextLine().trim();

                        try {
                            this.client.ajouterAuPanier(livreChoisi, this.client.getLibrairie(), Integer.parseInt(nombreCopies));
                        } 
                        catch (NumberFormatException e) {
                            System.out.println("Veuillez entrer un nombre valide de copies.");
                        } 
                        catch (QuantiteInvalideException e){
                            System.out.println("La quantité demandée est invalide pour le montant : " + nombreCopies);
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
        System.out.println("== Consultation du panier ==");
    }

    public void changementLibClient() {
        System.out.println("== Changement de librairie ==");
    }

    public void menuGestionLibrairie() {
        System.out.println("== Menu Gestion Librairie ==");
    }

    public void menuAjoutLivre() {
        System.out.println("== Ajout d’un livre au stock ==");
    }

    public void menuTransfertLivre() {
        System.out.println("== Transfert d’un livre ==");
    }

    public void menuUpdateInfoClient() {
        System.out.println("== Mise à jour des infos client ==");
    }

    public void menuCommande() { 
        System.out.println("== Passer une commande ==");
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

    private <T> void page(List<T> data){

        int index = 1;

        for (T toDisplay : data) {

            String toDisplayString = "  " + index + "." + toDisplay.toString();
            if(toDisplayString.length() > 62) {
                toDisplayString = toDisplayString.substring(0, 59) + "...";
            }
            else{
                toDisplayString = toDisplayString + " ".repeat(62 - toDisplayString.length()); // Ajustement de la longueur pour l'affichage
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
    }
}

