import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Client extends Personne{
    
    private final int idClient;
    private String address;
    private String codePostal;
    private String ville;
    private Integer idLibrairie;
    private Panier panier;


    /**
     * Constructeur de la classe Client
     * @param nom : String
     * @param prenom : String
     * @param prenom : motDePasse
     * @param id : int
     * @param address : String
     * @param codePostal : String
     * @param ville : String
     * @param librairie : Librairie
     */
    public Client(String nom, String prenom, String motDePasse, Integer id, String address, String codePostal, String ville, int idLibrairie) {
        super(nom, prenom, motDePasse);
        this.idClient = id;  
        this.address = address;
        this.codePostal = codePostal;
        this.ville = ville;

        this.idLibrairie = idLibrairie;
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
     * @return id de la librairie liée au client : int
     */
    public Integer getLibrairie() {
        return idLibrairie;
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
    public void setLibrairie(Integer idLibrairie) {
        this.idLibrairie = idLibrairie;
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
        try {
            return Reseau.getLibrairie(this.idLibrairie).consulterStock();
        } catch (LibraryNotFoundException e) {
            System.err.println("Erreur : librairie non trouvée (" + e.getMessage() + ")");
            return null;
        }
    }

    /**
     * ajoute un livre au panier du client
     * 
     * @param livre : Livre
     * @param librairie : Librairie
     * @param qte : int
     */
    public void ajouterAuPanier(Livre livre, int librairie, int qte) throws QuantiteInvalideException{
        panier.ajouterLivre(livre,librairie,qte);
    }

    /**
     * retire un livre du panier du client
     * 
     * @param livre : Livre
     * @param librairie : Librairie
     * @param qte : int
     */
    public void retirerDuPanier(Livre livre,int librairie,int qte) throws PasAssezDeStockException {
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
    public boolean commander(String modeLivraison,boolean enligne,boolean faireFacture) {

        // assurer que les stocks sont a jour
        Reseau.updateInfos(EnumUpdatesDB.STOCKS);

        if (!this.panier.getContenu().isEmpty()) {
            List<Commande> commandes = createCommandes(modeLivraison,enligne);

            for (Commande commande : commandes) {
                Reseau.enregisterCommande(commande);
            }

            this.panier.viderPanier();
            Reseau.updateInfos(EnumUpdatesDB.STOCKS);

            if(faireFacture){
                Reseau.createBillPDF(commandes, this, "."); // current directory
            }

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
    private List<Commande> createCommandes(String livraison,boolean enligne) {

        int nbDetailCommande = 0;
        int nbCommande = 0;
        int commandeError = 0;
        List<Commande> commandes = new ArrayList<>();

        for (int librairiePanier : this.panier.getContenu().keySet()) {

            try {
                Reseau.getLibrairie(idLibrairie);   // assurer que la librairie existe encore    
            } 
            catch (LibraryNotFoundException e) {
                commandeError += this.panier.getContenu().get(librairiePanier).size();
                continue; // on passe
            }

            Map<Livre, Integer> livres = this.panier.getContenu().get(librairiePanier);

            String typeCommande;
            if(enligne){ typeCommande = "O";}
            else{ typeCommande = "N";}


            Commande commande = new Commande(Reseau.numCom + nbCommande, new Date(),typeCommande,livraison,this.idClient, librairiePanier);
            nbCommande++;

            for(Livre livre : livres.keySet()) {

                int quantite = livres.get(livre);

                // Vérification de la quantité
                try {
                    if(!Reseau.checkStock(livre, Reseau.getLibrairie(librairiePanier), quantite)){
                        System.out.println("Erreur lors de l'ajout du livre " + livre.getTitre() + " pour une qte = " + quantite + " à la commande, dû a un stock insuffisant de la librairie : "+ librairiePanier +".");
                        commandeError++;
                        continue;
                    }
                } catch (LibraryNotFoundException e) {
                    e.printStackTrace();
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
            if(!commande.getDetails().isEmpty()) {
                commandes.add(commande);
            }
        }
        System.out.println("Nombre de commandes non enregistrées : " + commandeError);
        Reseau.updateInfos(EnumUpdatesDB.NUMCOM);
        Reseau.numCom += nbCommande;
        Reseau.numlig += nbDetailCommande;
        return commandes;
    }

    // methode de base selon la classification des livres
    public List<Livre> OnVousRecommande(int nbRecommandation) throws LibraryNotFoundException{

        Reseau.updateInfos(EnumUpdatesDB.STOCKS);

        Set<Livre> userBooks = Reseau.getUserBooks(this.idClient); 
        Set<String> criteria = this.getClassificationsCriteria(userBooks);

        Map<Livre,Set<Integer>> mapCommandesClients = Reseau.mapperCommandesClients(this.idClient);

        // traitement des livres respectant les critères

        Map<Livre,Set<Integer>> copyMapCommandesClients= new HashMap<>(mapCommandesClients);

        for(Livre book : copyMapCommandesClients.keySet()){
            if(!criteria.contains(book.getClassification())){
                mapCommandesClients.remove(book); // livre non conforme au critère
            }
        }

        // créer la liste des livres selon popularité
        List<Livre> popularBooks = this.getPopularBooks(mapCommandesClients);
        mapCommandesClients = null;

        // verif si livre pas commandé par les clients et livre en stock dans librairie courante

        Librairie currentLibrary = Reseau.getLibrairie(this.idLibrairie);

        List<Livre> recommanded = new ArrayList<>();

        for(int i=0;i<nbRecommandation;i++){
            if(i < popularBooks.size()){
                Livre book = popularBooks.get(i);
                if(!userBooks.contains(book) && currentLibrary.checkStock(book, 1)){
                    recommanded.add(book);
                }
            }
        }

        return recommanded;
    }

    private Set<String> getClassificationsCriteria(Set<Livre> books){

        Set<String> classifications = new HashSet<>();

        for(Livre book : books){
            classifications.add(book.getClassification());
        }

        return classifications;
    }

    private List<Livre> getPopularBooks(Map<Livre,Set<Integer>> booksMap){

        List<Livre> popularBooks = new ArrayList<>();
        Integer maxPop = null;
        Livre popLivre = null; 

        while(!booksMap.isEmpty()){

            for(Livre book : booksMap.keySet()){

                int pop =  booksMap.get(book).size();

                if((maxPop == null || pop > maxPop) && !popularBooks.contains(book)){
                    maxPop = pop;
                    popLivre = book;
                }
            }

            popularBooks.add(popLivre);
            maxPop = null;
            booksMap.remove(popLivre);
        }
        return popularBooks;
    }

    /**
     * permet de modifier les informations du client en BD
     * @param info : EnumInfoClient
     * @param data : String
     * @throws SQLException
     */
    public void modifInfo(EnumInfoClient info,String data) throws SQLException{

        switch (info) {

            case EnumInfoClient.NOM -> {

                PreparedStatement statement = Reseau.createStatement("UPDATE testCLIENT SET nomcli = ? WHERE idcli = ?");
                statement.setString(1, data);
                statement.setInt(2, this.idClient);
                statement.executeUpdate();
                this.setNom(data);
            }

            case EnumInfoClient.PRENOM -> {

                PreparedStatement statement = Reseau.createStatement("UPDATE testCLIENT SET prenomcli = ? WHERE idcli = ?");
                statement.setString(1, data);
                statement.setInt(2, this.idClient);
                statement.executeUpdate();
                this.setPrenom(data);
            }

            case EnumInfoClient.ADDRESS -> {
                PreparedStatement statement = Reseau.createStatement("UPDATE testCLIENT SET adressecli = ? WHERE idcli = ?");
                statement.setString(1, data);
                statement.setInt(2, this.idClient);
                statement.executeUpdate();
                this.address = data;
            }

            case EnumInfoClient.MDP -> {
                PreparedStatement statement = Reseau.createStatement("UPDATE testCLIENT SET motdepassecli = ? WHERE idcli = ?");
                statement.setString(1, data);
                statement.setInt(2, this.idClient);
                statement.executeUpdate();
                this.setMotDePasse(data);

            }

            case EnumInfoClient.VILLE -> {

                PreparedStatement statement = Reseau.createStatement("UPDATE testCLIENT SET villecli = ? WHERE idcli = ?");
                statement.setString(1, data);
                statement.setInt(2, this.idClient);
                statement.executeUpdate();
                this.ville = data;
            }   
             
            case EnumInfoClient.CODEPOSTAL -> {

                PreparedStatement statement = Reseau.createStatement("UPDATE testCLIENT SET codepostal = ? WHERE idcli = ?");
                statement.setString(1, data);
                statement.setInt(2, this.idClient);
                statement.executeUpdate();
                this.codePostal = data;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Client)) return false;

        Client client = (Client) obj;

        return this.idClient == client.idClient;
    }

    @Override
    public int hashCode(){
        return this.idClient * 7 + this.getNom().hashCode() * 11 + this.getPrenom().hashCode()*19;
    }

    /**
     * permet l'affichage du client
     */
    @Override
    public String toString() {
        return "Client " +
                "idClient=" + idClient +
                ", address='" + address + '\'' +
                ", codePostal='" + codePostal + '\'' +
                ", ville='" + ville + '\'';
    }
}