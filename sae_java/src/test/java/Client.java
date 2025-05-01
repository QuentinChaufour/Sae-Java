import java.util.HashMap;
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
    public void ajouterAuPanier(Livre livre, Librairie librairie, int qte) {
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
