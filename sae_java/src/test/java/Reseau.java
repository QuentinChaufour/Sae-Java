
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
            Reseau.updateInfos(EnumUpdatesDB.STOCKS);
            Reseau.updateInfos(EnumUpdatesDB.NUMCOM);
        }
        catch (SQLException e) {
        }

    }

    private Reseau() {} // Constructeur privé pour empêcher l'instanciation de la classe


    /**
     * met a jour les informations par rapport a la BD
     * 
     * @param update : EnumUpdatesDB
     * 
     */
    public static void updateInfos(EnumUpdatesDB update){

        switch (update) {
            case EnumUpdatesDB.LIBRAIRIE -> {
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

            case EnumUpdatesDB.STOCKS -> {
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
            PreparedStatement statementCommande = Reseau.connection.prepareStatement("INSERT INTO testCOMMANDE (numcom, datecom, enligne, livraison, idcli, idmag) VALUES (?, ?, ?, ?, ?, ?)");
            statementCommande.setInt(1, commande.getNumCommande());

            Date sqlDate = new java.sql.Date(commande.getDate().getTime());

            statementCommande.setDate(2, sqlDate);

            statementCommande.setString(3,"O");             // le client a fait sa commande nécessairement en ligne

            statementCommande.setString(4, commande.getLivraison()); 
            statementCommande.setInt(5, commande.getId());
            statementCommande.setString(6, commande.getIdLibrairie() + "");

            statementCommande.executeUpdate();
            statementCommande.close();
            

            for(DetailCommande detail : commande.getDetails()) {

                Librairie librairie = Reseau.librairies.get(commande.getIdLibrairie());

                if(librairie.checkStock(detail.getLivre(), detail.getQuantite())){
                    PreparedStatement statementDetail = connection.prepareStatement("INSERT INTO testDETAILCOMMANDE (numcom, numlig, qte, prixvente, isbn) VALUES (?, ?, ?, ?, ?)");

                    double prix = detail.getLivre().getPrix() * detail.getQuantite();

                    statementDetail.setInt(1, commande.getNumCommande());
                    statementDetail.setInt(2, detail.getNumLig());
                    statementDetail.setInt(3, detail.getQuantite());
                    statementDetail.setBigDecimal(4, new BigDecimal(prix));
                    statementDetail.setString(5, detail.getLivre().getIsbn());

                    statementDetail.executeUpdate();
                    statementDetail.close();

                    try {
                        librairie.retirerLivre(detail.getLivre(), detail.getQuantite()); // Met à jour le stock de la librairie
                    } catch (QuantiteInvalideException e) {
                        System.out.println("Erreur : Pas assez de stock pour le livre " + detail.getLivre().getTitre());
                    } catch (BookNotInStockException e) {
                        System.out.println("Erreur : Livre " + detail.getLivre().getTitre() + " non trouvé dans le stock de la librairie.");
                    }
                }
                else {
                    System.out.println("Erreur : Pas assez de stock pour le livre " + detail.getLivre().getTitre());
                }

            }
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données : " + e.getMessage());
        }
    }

}
