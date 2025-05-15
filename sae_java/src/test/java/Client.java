import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Client extends Personne{
    
    private int idClient;
    private String address;
    private String codePostal;
    private String ville;
    private Librairie librairie;
    private Panier panier;


    /**
     * Constructeur de la classe Client
     * @param nom : String
     * @param prenom : String
     * @param id : int
     * @param address : String
     * @param codePostal : String
     * @param ville : String
     * @param librairie : Librairie
     */
    public Client(String nom, String prenom,int id, String address, String codePostal, String ville, Librairie librairie) {
        super(nom, prenom);
        this.idClient = id;  
        this.address = address;
        this.codePostal = codePostal;
        this.ville = ville;

        this.librairie = librairie;
        this.panier = new Panier();
    }

    /**
     * getteur de l'id du client
     * 
     * @return l'id du client : int
     */
    public int getId() {
        return idClient;
    }

    /**
     * getteur de l'adresse du client
     * 
     * @return l'adresse du client : String
     */
    public String getAddress() {
        return address;
    }

    /**
     * getteur du code postal du client
     * 
     * @return le code postal du client : String
     */
    public String getCodePostal() {
        return codePostal;
    }

    /**
     * getteur de la ville du client
     * 
     * @return la ville du client : String
     */
    public String getVille() {
        return ville;
    }

    /**
     * getteur de la librairie liée au client
     * 
     * @return la librairie liée au client : Librairie
     */
    public Librairie getLibrairie() {
        return librairie;
    }

    /**
     * setteur de l'adresse du client
     * 
     * @param address : String
     */
    public void setAddresse(String address) {
        this.address = address;
    }

    /**
     * setteur du code postal du client
     * 
     * @param codePostal : String
     */
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    /**
     * setteur de la ville du client
     * 
     * @param ville : String
     */
    public void setVille(String ville) {
        this.ville = ville;
    }

    /**
     * setteur de la librairie liée au client
     * 
     * @param librairie
     */
    public void setLibrairie(Librairie librairie) {
        this.librairie = librairie;
    }

    /**
     * getteur du panier du client
     * 
     * @return le panier du client : Panier
     */
    public Panier getPanier() {
        return this.panier;
    }

    public Map<Livre, Integer> consulterLivres() {
        return new HashMap<>(this.librairie.consulterStock());
    }

    /**
     * ajoute un livre au panier du client
     * 
     * @param livre : Livre
     * @param librairie : Librairie
     * @param qte : int
     */
    public void ajouterAuPanier(Livre livre, Librairie librairie, int qte) throws QuantiteInvalideException{
        panier.ajouterLivre(livre,librairie,qte);
    }

    /**
     * retire un livre du panier du client
     * 
     * @param livre : Livre
     * @param librairie : Librairie
     * @param qte : int
     */
    public void retirerDuPanier(Livre livre,Librairie librairie,int qte) throws PasAssezDeStockException {
        this.panier.retirerLivre(livre,librairie,qte);
    }

    /**
     * vide le panier du client
     * 
     */
    public void viderPanier() {
        this.panier.viderPanier();
    }

    /**
     * affiche le contenu du panier du client
     * @return le panier : String
     */
    public String afficherPanier() {
        return this.panier.toString();
    }


    /**
     *  permet a un client de passer commande de son panier
     * @return si les commandes ont été éffectué mais pas si elles étaient correctent
     */
    public boolean commander() {

        String livraison = "O";

        // assurer que les stocks sont a jour
        Reseau.updateInfos(EnumUpdatesDB.STOCKS);

        if (!this.panier.getContenu().isEmpty()) {
            List<Commande> commandes = createCommandes(livraison);

            for (Commande commande : commandes) {
                Reseau.enregisterCommande(commande);
            }

            this.panier.viderPanier();
            Reseau.updateInfos(EnumUpdatesDB.STOCKS);
        } else {
            System.out.println("Le panier est vide.");
            return false;
        }

        return true;
    }

    /**
     * crée la liste des commandes du panier du client
     * 
     * @param livraison : String
     * @return la liste des commandes du panier du client
     */
    private List<Commande> createCommandes(String livraison) {

        int nbDetailCommande = 0;
        int nbCommande = 0;
        int commandeError = 0;
        List<Commande> commandes = new ArrayList<>();

        for (Librairie librairiePanier : this.panier.getContenu().keySet()) {

            Map<Livre, Integer> livres = this.panier.getContenu().get(librairiePanier);

            Commande commande = new Commande(Reseau.numCom + nbCommande, new Date(),"O",livraison,this, librairiePanier);
            nbCommande++;

            for(Livre livre : livres.keySet()) {

                int quantite = livres.get(livre);

                // Vérification de la quantité
                if(!Reseau.checkStock(livre, Reseau.librairies.get(Reseau.librairies.indexOf(librairiePanier)), quantite)){
                    System.out.println("Erreur lors de l'ajout du livre " + livre.getTitre() + " à la commande, dû a un stock insuffisant de la librairie : "+ librairiePanier +".");
                    commandeError++;
                    continue;
                }

                // si la quantité est valide, on l'ajoute à la commande
                try{
                    DetailCommande detail = new DetailCommande(Reseau.numlig + nbDetailCommande, livre, quantite);
                    commande.addDetailCommande(detail);
                    nbDetailCommande++;
                }
                catch (QuantiteInvalideException e) {
                    System.out.println("Erreur lors de l'ajout du livre " + livre.getTitre() + " à la commande, dû a une quantité selectionnée invalide.");
                    commandeError++;
                }
            }
            commandes.add(commande);
        }
        System.out.println("Nombre de commandes non enregistrées : " + commandeError);
        Reseau.updateInfos(EnumUpdatesDB.NUMCOM);
        return commandes;
    }

    /**
     * permet l'affichage du client
     */
    @Override
    public String toString() {
        return "Client{" +
                "idClient=" + idClient +
                ", address='" + address + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'' +
                ", librairie=" + librairie.getNom() +
                '}';
    }
}
