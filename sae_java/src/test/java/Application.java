import java.util.Scanner;

public class Application {

    private Scanner scanner = new Scanner(System.in);

    public void menuIdentification() {
        boolean actif = true;
        
        while (actif) {
            System.out.println("== Identification ==");
            System.out.println("1. Client");
            System.out.println("2. Vendeur");
            System.out.println("3. Administrateur");
            System.out.println("q. Quitter");
            System.out.print("Votre choix : ");

            String choix = scanner.nextLine();
            switch (choix) {
                case "1":
                    System.out.println("1. Avez vous un compte client ?");
                    break;
                case "2":
                    menuIdentification();
                    break;
                case "3":
                    menuIdentification();
                    break;
                case "4":
                    actif = false;
                    System.out.println("Fermeture de l'application.");
                    break;
                default:
                System.out.println("Choix invalide.");
                
            }
        }
    }

    public void menuIdentificationClient() {
        boolean actif = true;
        
        while (actif) {
            System.out.println("== Identification ==");
          
                case "4":
                    actif = false;
                    System.out.println("Fermeture de l'application.");
                    break;
                default:
                System.out.println("Choix invalide.");
                
            }
        }
    }
    public void menuPClient() {
        System.out.println("== Menu Client ==");
    }

    public void menuPVendeur() {
        System.out.println("== Menu Vendeur ==");
    }

    public void menuConsultationBooks() {
        System.out.println("== Consultation du catalogue ==");
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

    public void lancerApplication() {
        boolean actif = true;

        while (actif) {
            System.out.println("\n=== LivreExpress – Menu Principal ===");
            System.out.println("1. Identification");
            System.out.println("2. Quitter");
            System.out.print("Votre choix : ");

            String choix = scanner.nextLine();
            switch (choix) {
                case "1":
                    menuIdentification();
                    break;
                case "2":
                    actif = false;
                    System.out.println("Fermeture de l'application.");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    public static void main(String[] args) {
        Application app = new Application();
        app.lancerApplication();
    }

