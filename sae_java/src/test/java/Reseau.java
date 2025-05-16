
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Reseau {

    public static final String DB_URL = "jdbc:mariadb://localhost:3306/testLibrairie";

    public static List<Librairie> librairies = new ArrayList<>();

    private static Connection connection = null;
    
    public static int numCom = 1; // en cas de table vide (pas de commande)
    public static int numlig = 1; // en cas de table vide 

    // initialisation des valeurs de BD
    static {
        try {
            Reseau.connection = DriverManager.getConnection(DB_URL, "root", "root_password");

            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);
            Reseau.updateInfos(EnumUpdatesDB.NUMCOM);
        }
        catch (SQLException e) {
            System.exit(2);
        }

    }

    private Reseau() {} // Constructeur privé pour empêcher l'instanciation de la classe

    public static Librairie getLibrairie(int idLibrairie) throws LibraryNotFoundException{
        for(Librairie lib : Reseau.librairies){
            if(lib.getId() == idLibrairie){
                return lib;
            }
        }
        throw new LibraryNotFoundException();
    }

    /**
     * met a jour les informations par rapport a la BD
     * 
     * @param update : EnumUpdatesDB
     * 
     */
    public static void updateInfos(EnumUpdatesDB update){

        switch (update) {
            case EnumUpdatesDB.LIBRAIRIE,EnumUpdatesDB.STOCKS -> {

                try{
                    // Requête pour update les librairies
                    PreparedStatement statement = Reseau.connection.prepareStatement("SELECT * FROM testMAGASIN");

                    List<Librairie> newLibrairies = new ArrayList<>();

                    ResultSet resultSet = statement.executeQuery();

                    while(resultSet.next()){
                        int idLibrairie = resultSet.getInt("idmag");
                        String nomLibrairie = resultSet.getString("nommag");
                        String villeLibrairie = resultSet.getString("villemag");

                        newLibrairies.add(new Librairie(idLibrairie, nomLibrairie, villeLibrairie));
                    }

                    Reseau.librairies = newLibrairies;
                }
                catch (SQLException e){
                    System.err.println("problème est survenu lors de l'update des Librairies");
                }

                // mise a jour des stocks

                try {
                    
                    for(Librairie lib : Reseau.librairies){
                        PreparedStatement statement = Reseau.connection.prepareStatement("SELECT * FROM testMAGASIN NATURAL JOIN testPOSSEDER NATURAL JOIN testLIVRE NATURAL JOIN testAUTEUR WHERE idmag = ?");

                        statement.setInt(1,lib.getId());

                        ResultSet resultSet = statement.executeQuery();

                        while(resultSet.next()){

                            String isbn = resultSet.getString("isbn");
                            int qte = resultSet.getInt("qte");

                            String titreLivre = resultSet.getString("titre");
                            int nbPages = resultSet.getInt("nbpages");
                            int datePubli = resultSet.getInt("datepubli");
                            double prix = resultSet.getBigDecimal("prix").doubleValue();
                            String nomClass = resultSet.getString("nomclass");
                            String nomEdit = resultSet.getString("nomedit");

                            Livre livre = new Livre(isbn, titreLivre, nomEdit, datePubli, prix, nbPages, nomClass);

                            List<Auteur> auteurs = new ArrayList<>();

                            String idAuteur = resultSet.getString("idauteur");
                            String nomPrenom = resultSet.getString("nomauteur");
                            Integer naissance = resultSet.getInt("anneenais");
                            Integer deces = resultSet.getInt("anneedeces");   
                            auteurs.add(new Auteur(idAuteur, nomPrenom, naissance, deces));

                            while(resultSet.next() && resultSet.getString("isbn").equals(isbn)){

                                idAuteur = resultSet.getString("idauteur");
                                nomPrenom = resultSet.getString("nomauteur");
                                naissance = resultSet.getInt("anneenais");
                                deces = resultSet.getInt("anneedeces");   

                                auteurs.add(new Auteur(idAuteur, nomPrenom, naissance, deces));
                                
                            }

                            if(resultSet.next() && !resultSet.getString("isbn").equals(isbn)){
                                resultSet.previous();
                                resultSet.previous();
                            }

                            for(Auteur auteur : auteurs){
                                livre.ajouterAuteur(auteur);
                            }
                            
                            try {
                                lib.ajouterLivre(livre, qte);
                            } catch (QuantiteInvalideException e) {
                                System.err.println("Qte invalide");
                            }
                        }
                    }

                } catch (SQLException e) {
                    System.err.println("problème est survenu lors de l'update des stocks");
                }

            }
            case EnumUpdatesDB.NUMCOM,EnumUpdatesDB.NUMLIG -> { // liée donc update coordonnée
                try{

                    // Récupère le plus grand numCom existant
                    PreparedStatement statement = Reseau.connection.prepareStatement("SELECT numcom FROM testCOMMANDE where numcom >= ALL (SELECT numcom FROM testCOMMANDE)");

                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        Reseau.numCom = resultSet.getInt("numcom") + 1; 
                    }
                
                    PreparedStatement statementNumLig = Reseau.connection.prepareStatement("SELECT numlig FROM testDETAILCOMMANDE where numlig >= ALL (SELECT numlig FROM testDETAILCOMMANDE)");
                
                    resultSet = statementNumLig.executeQuery();
                
                
                    if (resultSet.next()) {
                        Reseau.numlig = resultSet.getInt("numlig") + 1; 
                    }
                }
                catch(SQLException e){

                }
            }
            default -> throw new AssertionError();
        }
    }

    /**
     * ajoute une librairie au reseaux
     * 
     * @param librairie
     */
    public static void addLibrairie(Librairie librairie) {
        librairies.add(librairie);

        // ajouter partie BD
    }

    /**
     * retire une librairie du reseaux
     * 
     * @param librairie
     **/
    public void removeLibrairie(Librairie librairie) throws LibraryNotFoundException {
        if (librairies.contains(librairie)) {
            librairies.remove(librairie);
        }
        else {
            throw new LibraryNotFoundException();
        }
    }



    /**
     * vérifie les stocks de livres pour une certaine librairie
     * 
     * @param livre : Livre
     * @param librairie : Librairie
     * @param qte : int
     * @return si la quantité est inférieur ou égal au stocks, false sinon
     */
    public static boolean checkStock(Livre livre, Librairie librairie, int qte) {
        return librairie.checkStock(livre, qte);
    }

    /**
     * enregiste une commande en BD
     * 
     * @param commande : Commande
     */
    public static void enregisterCommande(Commande commande) {
        
        try {
            PreparedStatement statementCommande = Reseau.connection.prepareStatement("INSERT INTO testCOMMANDE VALUES (?, ?, ?, ?, ?, ?)");
            statementCommande.setInt(1, commande.getNumCommande());

            Date sqlDate = new java.sql.Date(commande.getDate().getTime());

            statementCommande.setDate(2, sqlDate);

            statementCommande.setString(3,"O");             // le client a fait sa commande nécessairement en ligne

            statementCommande.setString(4, commande.getLivraison()); 
            statementCommande.setInt(5, commande.getId());
            statementCommande.setInt(6, commande.getIdLibrairie());

            statementCommande.executeUpdate();
            statementCommande.close();
            

            for(DetailCommande detail : commande.getDetails()) {
                try{
                    Librairie librairie = Reseau.getLibrairie(commande.getIdLibrairie());
                
                    if(librairie.checkStock(detail.getLivre(), detail.getQuantite())){
                        PreparedStatement statementDetail = Reseau.connection.prepareStatement("INSERT INTO testDETAILCOMMANDE (numcom, numlig, qte, prixvente, isbn) VALUES (?, ?, ?, ?, ?)");

                        double prix = detail.getLivre().getPrix() * detail.getQuantite();

                        statementDetail.setInt(1, commande.getNumCommande());
                        statementDetail.setInt(2, detail.getNumLig());
                        statementDetail.setInt(3, detail.getQuantite());
                        statementDetail.setBigDecimal(4, new BigDecimal(prix));
                        statementDetail.setString(5, detail.getLivre().getIsbn());

                        statementDetail.executeUpdate();
                        statementDetail.close();

                        // update DB'Stocks
                        PreparedStatement statement  = Reseau.connection.prepareStatement("UPDATE testPOSSEDER SET qte = ? WHERE idmag = ? AND isbn = ?");

                        statement.setInt(1,librairie.consulterStock().get(detail.getLivre()) - detail.getQuantite());
                        statement.setInt(2,librairie.getId());
                        statement.setString(3, detail.getLivre().getIsbn());

                        statement.executeUpdate();

                    }
                    else {
                        System.out.println("Erreur : Pas assez de stock pour le livre " + detail.getLivre().getTitre());
                    }
                } 
                catch(SQLException e){
                    System.out.println("Pb DB");
                }
                catch(LibraryNotFoundException e){
                    System.out.println("La librairie : " + commande.getIdLibrairie() +" n'est pas présente");
                }
            }
        }
        catch (SQLException e){

        }
    }

    // methode uniquement pour les tests
    public static PreparedStatement createStatement(String request){
        try {
            return  Reseau.connection.prepareStatement(request);
        } catch (SQLException e) {
            return null;
        }
    }

}
