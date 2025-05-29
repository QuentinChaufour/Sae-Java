
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class Reseau {

    private static final String DB_URL = "jdbc:mariadb://localhost:3306/testLibrairie";

    // compte admin

    private static final String ADMIN_USER = "revan";
    private static final String ADMIN_PASSWORD = "Two_Face";

    public static List<Librairie> librairies = new ArrayList<>();

    private static Connection connection = null;

    public static int numCom = 1; // en cas de table vide (pas de commande)
    public static int numlig = 1; // en cas de table vide 

    // initialisation des valeurs de BD
    static {
        try {
            Reseau.connection = DriverManager.getConnection(Reseau.DB_URL, "root", "root_password");

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

    public static Connection getConnection(){
        return Reseau.connection;
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
                    Collections.sort(newLibrairies); // Tri des librairies par ID pour l'affichage
                    Reseau.librairies = newLibrairies;

                    statement.close();
                }
                catch (SQLException e){
                    System.err.println("problème est survenu lors de l'update des Librairies");
                }

                // mise a jour des stocks

                try {
                    
                    for(Librairie lib : Reseau.librairies){

                        PreparedStatement statement = Reseau.connection.prepareStatement("SELECT * FROM testPOSSEDER WHERE idmag = ?");

                        statement.setInt(1,lib.getId());

                        ResultSet resultSet = statement.executeQuery();

                        Map<String,Integer> stocks = new HashMap<>();

                        while(resultSet.next()){

                            String isbn = resultSet.getString("isbn");
                            Integer qte = resultSet.getInt("qte");

                            stocks.put(isbn,qte);
                        }

                        statement.close();

                        for(String isbn : stocks.keySet()){

                            Livre livre = Reseau.createLivre(isbn);

                            try {
                                lib.ajouterAuStock(livre, stocks.get(isbn));
                            } catch (QuantiteInvalideException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (SQLException e) {
                    System.err.println("problème est survenu lors de l'update des stocks");
                    e.printStackTrace();
                }
                Collections.sort(Reseau.librairies);

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
                    statement.close();
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
    public static void addLibrairie(Librairie librairie) throws SQLException{

        if (!librairies.contains(librairie)) {
            librairies.add(librairie);
        }
    }

    public static void addNewLibrairie(Librairie librairie) throws SQLException, LibraryAlreadyExistsException {
        if (librairies.contains(librairie)) {
            throw new LibraryAlreadyExistsException("La librairie " + librairie.getNom() + " existe déjà dans le réseau.");
        } else {
            PreparedStatement statement = Reseau.connection.prepareStatement("INSERT INTO testMAGASIN VALUES (?, ?, ?)");
            statement.setInt(1, librairie.getId());
            statement.setString(2, librairie.getNom());
            statement.setString(3, librairie.getVille());
            statement.executeUpdate();
            statement.close();

            librairies.add(librairie);
        }
    }

    /**
     * retire une librairie du reseaux
     * 
     * @param librairie
     **/
    public static void removeLibrairie(Librairie librairie) throws LibraryNotFoundException,SQLException {
        if (librairies.contains(librairie)) {
                
                PreparedStatement statementMagasin = Reseau.connection.prepareStatement("DELETE FROM testMAGASIN WHERE idmag = ?");
                PreparedStatement statementLivre = Reseau.connection.prepareStatement("DELETE FROM testPOSSEDER WHERE idmag = ?");
                statementMagasin.setInt(1, librairie.getId());
                statementLivre.setInt(1, librairie.getId());
                statementMagasin.executeUpdate();
                statementLivre.executeUpdate();
                statementMagasin.close();
                statementLivre.close();

                librairies.remove(librairie);
        }
        else {
            throw new LibraryNotFoundException();
        }
    }

    /**
     * Cherche un livre dans une certaine quantité dans l'ensemble des librairies du réseau
     * 
     * @param librairie
     **/
    public Librairie findLivre(Livre livre, int qte) {
        for (Librairie lib : librairies) {
            if (lib.checkStock(livre, qte)) {
                return lib;
            }
        }
        return null;
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
                        statement.close();

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

                statementCommande.close();
            }
        }
        catch (SQLException e){

        }
    }


    public static Set<Livre> getUserBooks(int idClient){

        Set<Livre> userBooks = new HashSet<>();

        try {

            PreparedStatement statement = Reseau.connection.prepareStatement("SELECT * FROM testCOMMANDE NATURAL JOIN testDETAILCOMMANDE WHERE idcli = ?");
            statement.setInt(1,idClient);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){

                String isbn = resultSet.getString("isbn");

                Livre livre = Reseau.createLivre(isbn);
                userBooks.add(livre);
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userBooks;
    }

    public static Map<Livre,Set<Integer>> mapperCommandesClients(int idClientToAvoid){

        Map<Livre,Set<Integer>> clientsBooks = new HashMap<>(); // livre et set d'ID client pour connaitre la popularité d'un livre reflété par les commandes

        try {
            PreparedStatement statement = Reseau.connection.prepareStatement("SELECT * FROM testCOMMANDE NATURAL JOIN testDETAILCOMMANDE WHERE idcli <> ?");
            statement.setInt(1,idClientToAvoid);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){

                String isbn = resultSet.getString("isbn");
                int idClient = resultSet.getInt("idcli");

                Livre livre = Reseau.createLivre(isbn);

                Set<Integer> clients = clientsBooks.getOrDefault(livre, new HashSet<>());
                clients.add(idClient);
                clientsBooks.put(livre,clients);

            }

            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientsBooks;
    }

    private static Livre createLivre(String isbn) throws SQLException{
        
        PreparedStatement statementLivre = Reseau.connection.prepareStatement("SELECT * FROM testLIVRE WHERE isbn = ?");
        statementLivre.setString(1, isbn);
        ResultSet resultSetLivre = statementLivre.executeQuery();

        resultSetLivre.next();

        String titreLivre = resultSetLivre.getString("titre");
        Integer nbPages = resultSetLivre.getInt("nbpages");
        Integer datePubli = resultSetLivre.getInt("datepubli");
        double prix = resultSetLivre.getBigDecimal("prix").doubleValue();
        String nomClass = resultSetLivre.getString("nomclass");
        String nomEdit = resultSetLivre.getString("nomedit");

        Livre livre = new Livre(isbn, titreLivre, nomEdit, datePubli, prix, nbPages, nomClass);

        PreparedStatement statementAutor = Reseau.connection.prepareStatement("SELECT * FROM testECRIRE NATURAL JOIN testAUTEUR WHERE isbn = ?");
        statementAutor.setString(1, isbn);

        ResultSet resultSetAutor = statementAutor.executeQuery();

        while (resultSetAutor.next()) {
            String idAuteur = resultSetAutor.getString("idauteur");
            String nomPrenom = resultSetAutor.getString("nomauteur");
            Integer naissance = resultSetAutor.getInt("anneenais");
            Integer deces = resultSetAutor.getInt("anneedeces");
            livre.ajouterAuteur(new Auteur(idAuteur, nomPrenom, naissance, deces));
        }

        statementAutor.close();
        return livre;
    }

        // identifications 

    public static Client identificationClient(String nom,String prenom, String motDePasse,int idLibrary) throws SQLException, NoCorrespondingClient{

        PreparedStatement statement = Reseau.connection.prepareStatement("SELECT * FROM testCLIENT WHERE nomcli = ? AND prenomcli = ? AND motdepassecli = ?");
        statement.setString(1, nom);
        statement.setString(2, prenom);
        statement.setString(3, motDePasse);
        
        ResultSet resultset = statement.executeQuery();
        if(resultset.next()){

            int idClient = resultset.getInt("idcli");
            String nomClient = resultset.getString("nomcli");
            String prenomClient = resultset.getString("prenomcli");
            String motDePasseClient = resultset.getString("motdepassecli");
            String adresseClient = resultset.getString("adressecli");
            String codePostal = resultset.getString("codepostal");
            String ville = resultset.getString("villecli");
            

            Client client = new Client(nomClient, prenomClient, motDePasseClient, idClient, adresseClient, codePostal, ville,idLibrary);
            return client;
        }
        else {
            throw new NoCorrespondingClient("Aucun client ne correspond selon les critères de recherche : " + prenom + " " + nom + " avec comme mot de passe " + motDePasse);
        }
        
    }

    public static boolean identificationAdmin(String userName, String motDePasse) {

        return userName.equals(Reseau.ADMIN_USER) && motDePasse.equals(Reseau.ADMIN_PASSWORD);
    }

    // fonction facture PDF

    /**
     * Crée un PDF de facture pour les commandes passées par un client
     * @param commandes : Commande
     * @param client : Client
     * @param fileName : String
     */
    public static void createBillPDF(List<Commande> commandes, Client client,String folderName) {

        try {

            Document facturePDF = new Document();
            double total = 0.0;

            String fileName = folderName + "/" + client.getNom() + "_" + client.getPrenom() + "_facture" + ".pdf";

            PdfWriter writer = PdfWriter.getInstance(facturePDF, new FileOutputStream(fileName));

            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);

            facturePDF.open();

            // client information
            Paragraph clientInfo = new Paragraph(
                    """
                    Informations du client : 
                    ID Client : """ + client.getId() + "    " +
                    "Nom : " + client.getNom() + "   " +
                    "Prénom : " + client.getPrenom() + "\n" +
                    "Adresse : " + client.getAddress() + "  " +
                    client.getCodePostal() + "   " +       
                    client.getVille() + "\n" +
                    "______________________________________________________________________________" + 
                    "\n",infoFont
                );

            facturePDF.add(clientInfo);

            // commandes information
            for(Commande commande : commandes) {

                String typeCommande = commande.getEnLigne();

                switch (typeCommande) {
                    case "O" -> typeCommande = "En ligne";
                    case "M" -> typeCommande = "En point de vente";
                    default -> typeCommande = "Inconnu";
                }

                String typeLivraison = commande.getLivraison();

                switch (typeLivraison) {
                    case "C" -> typeLivraison = "Livraison à domicile";
                    case "M" -> typeLivraison = "Retrait en librairie";
                    default -> typeLivraison = "Inconnu";
                }

                Paragraph commandeInfo = new Paragraph(
                        """
                        Informations de la commande :
                        Numéro de commande : """ + commande.getNumCommande() + "\n" +
                        "Date de la commande : " + commande.getDate() + "\n" +
                        "Livraison : " + typeLivraison + "  |  " +
                        "Type de commande : " + typeCommande + "\n\n" +
                        "Librairie : " + Reseau.getLibrairie(commande.getIdLibrairie()).getNom() + "\n\n",infoFont
                    );

                facturePDF.add(commandeInfo);

                // creation du tableau des détails de la commande

                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setSpacingBefore(11f);
                table.setSpacingAfter(11f);
                

                float[] columnWidths = {4f, 1f, 2f};
                table.setWidths(columnWidths);

                table.setHeaderRows(1);

                // entête du tableau
                Paragraph livreHeader = new Paragraph("Livre");

                PdfPCell livreHeaderCell = new PdfPCell(livreHeader);
                livreHeaderCell.setPadding(5f);
                livreHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                livreHeaderCell.setBackgroundColor(BaseColor.GRAY);

                Paragraph quantityHeader = new Paragraph("Quantité");
                quantityHeader.setAlignment(Paragraph.ALIGN_CENTER);
                PdfPCell quantityHeaderCell = new PdfPCell(quantityHeader);
                quantityHeaderCell.setPadding(5f);
                quantityHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                quantityHeaderCell.setBackgroundColor(BaseColor.GRAY);

                Paragraph priceHeader = new Paragraph("Total (€)");
                priceHeader.setAlignment(Paragraph.ALIGN_CENTER);
                PdfPCell priceHeaderCell = new PdfPCell(priceHeader);
                priceHeaderCell.setPadding(5f);
                priceHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                priceHeaderCell.setBackgroundColor(BaseColor.GRAY);

                table.addCell(livreHeaderCell);
                table.addCell(quantityHeaderCell);
                table.addCell(priceHeaderCell);

                // cellules pour les détails de la commande

                PdfPCell cellBook = new PdfPCell();
                cellBook.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cellBook.setPadding(5f);

                PdfPCell cellQuantity = new PdfPCell();
                cellQuantity.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cellQuantity.setPadding(5f);

                PdfPCell cellPrice = new PdfPCell();
                cellPrice.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                cellPrice.setPadding(5f);

                cellBook.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellQuantity.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellPrice.setBackgroundColor(BaseColor.LIGHT_GRAY);

                // détails de la commande
                for(DetailCommande detail : commande.getDetails()) {
                    Chunk detailInfo = new Chunk(
                        "ISBN : " + detail.getLivre().getIsbn() + "     " +
                        detail.getLivre().getTitre()
                    );

                    cellBook.addElement(detailInfo);

                    Paragraph quantityInfo = new Paragraph(detail.getQuantite());
                    quantityInfo.setAlignment(Paragraph.ALIGN_CENTER);
                    Paragraph priceInfo = new Paragraph((detail.getLivre().getPrix() * detail.getQuantite()) + " €");
                    priceInfo.setAlignment(Paragraph.ALIGN_RIGHT);

                    double prix = detail.getLivre().getPrix() * detail.getQuantite();

                    cellQuantity.addElement(quantityInfo);
                    cellPrice.addElement(priceInfo);
                }

                table.addCell(cellBook);
                table.addCell(cellQuantity);
                table.addCell(cellPrice);
                facturePDF.add(table);
                double totalCommande = commande.getTotalCommande();
                total += totalCommande;
                facturePDF.add(new Paragraph("Montant total de la commande : " + totalCommande + " €\n\n"));
                facturePDF.add(new Paragraph("______________________________________________________________________________\n\n"));
            }

            // total de toutes les commandes
            facturePDF.add(new Paragraph("Montant total de toutes les commandes : " + total + " €\n\n"));

            facturePDF.close();
            writer.close();
            
        } catch (DocumentException | FileNotFoundException e) {
            System.err.println("Erreur lors de la création du PDF : " + e.getMessage());
        }
        catch (LibraryNotFoundException e) {
            System.err.println("Erreur lors de la récupération de la librairie pour la création de la facture²: " + e.getMessage());
        }



        
    }


    public static PreparedStatement createStatement(String request){
        try {
            return  Reseau.connection.prepareStatement(request);
        } catch (SQLException e) {
            return null;
        }
    }

}
