
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Vendeur extends Personne{
    private int idVendeur;
    private int idlibrairie;

    public Vendeur(String nom, String prenom, String motDePasse, int id){
        super(nom, prenom, motDePasse);
        this.idVendeur = id;
    }

    public void ajouteLivreStock(Livre livre){        
        try {
            PreparedStatement ps = Reseau.createStatement("insert into testLIVRE values (?,?,?,?,?,?,?,?)");
            ps.setString(1, livre.getIsbn());
            ps.setString(2, livre.getTitre());
            ps.setObject(3, livre.getAuteurs());
            ps.setString(4, livre.getEditeur());
            ps.setInt(5, livre.getDatePublication());
            ps.setDouble(6, livre.getPrix());
            ps.setInt(7, livre.getNbPages());
            ps.setString(8, livre.getClassification());
            ps.executeUpdate();
            Reseau.updateInfos(EnumUpdatesDB.STOCKS);
        } catch (SQLException e) {
            e.printStackTrace();    
        } 
    }


    public boolean checkQte(Commande commande){
      Reseau.updateInfos(EnumUpdatesDB.STOCKS);
        for(DetailCommande detail : commande.getDetails()){
            try {
                if(!Reseau.checkStock(detail.getLivre(), Reseau.getLibrairie(commande.getIdLibrairie()), 1)){
                    return false;
                }
            } catch (LibraryNotFoundException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    
    public Panier createPanier(Map<Livre, Integer> listeLivres){
        Panier panier = new Panier();
        for(Livre livre : listeLivres.keySet()){
                panier.ajouterLivre(livre, this.idlibrairie, listeLivres.get(livre));
        }
        return panier;
    }


    public Commande preparerCommandes(int idClient, Map<Livre, Integer> listeLivres, Date date, String enLigne, String Livraison ){
        PreparedStatement s;
        int maxNumCommande = 0;
        System.out.println("Maxnumcommande1 : " + maxNumCommande);
        try{
            PreparedStatement statement = Reseau.createStatement("SELECT numcom FROM testCOMMANDE where numcom >= ALL (SELECT numcom FROM testCOMMANDE)");

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                maxNumCommande = resultSet.getInt("numcom") + 1; 
                System.out.println("Maxnumcommande2 : " + maxNumCommande);
            }
            statement.close();
        }
        catch(SQLException e){
            System.err.println("connexion SQL ECHOUE");
        }
        
        Commande commande = new Commande(maxNumCommande, date, enLigne, Livraison, idClient, this.idlibrairie);
        int cpt = 1;
        for(Livre livre : listeLivres.keySet()){
            try {
                commande.addDetailCommande(new DetailCommande(cpt, livre, listeLivres.get(livre)));
            } catch (QuantiteInvalideException e) {
                System.err.println("Erreur de quantité");
            }
            cpt++;
        }
        return commande;
    }

    public void enregistrerCommandes(List<Commande> commandes){
        // va faire l'insert des commandes dans la bd et soustraire les stocks
        for(Commande commande : commandes){
            Reseau.enregisterCommande(commande);
        Reseau.updateInfos(EnumUpdatesDB.STOCKS);
        }
    }

    public void passerCommande(Client client, boolean faireFacture, String livraison){
        //demander pour public de createCOmmandes
        List<Commande> commandesClient = new ArrayList<>();
        int maxNumCommande = 0;
        PreparedStatement s;
        try {
            try {
                s = Reseau.createStatement("SELECT MAX(numcom) FROM testCOMMANDE");
                ResultSet rs = s.executeQuery();
                maxNumCommande = rs.getInt(1)+1;
                if (rs.next()) {
                    maxNumCommande = rs.getInt(1) + 1;
                } else {
                    maxNumCommande = 1; 
                }
            } catch (SQLException e) {
                System.err.println("erreur connection SQL");
            }
            Reseau.getLibrairie(this.idlibrairie);
            Map<Livre, Integer> mapLivres = client.getPanier().getContenu().get(this.idlibrairie);
            Commande commande = new Commande(maxNumCommande, new Date(), "N", livraison, client.getId(), this.idlibrairie);
            for(Livre livre : mapLivres.keySet()){
                int cpt = 1;
                try {
                    commande.getDetails().add(new DetailCommande(cpt, livre, mapLivres.get(livre)));
                } catch (QuantiteInvalideException e) {
                    System.err.println("Quantite invalide");
                }
                cpt++;
            }
            Reseau.enregisterCommande(commande);
            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);
            if(faireFacture){
                Reseau.createBillPDF(commandesClient, client, livraison);
            }
        } catch (LibraryNotFoundException e) {
            System.err.println("Pas de librairie trouvée");
        }
        
    }

    public Livre transfererLivre(Livre livre, Librairie nouvLibrairie){
        try {
            PreparedStatement ps1 = Reseau.createStatement("insert into testLIVRE values (?, ?, ?, ?, ?, ?, ?, ?)");
            ps1.setString(1, livre.getIsbn());
            ps1.setString(2, livre.getTitre());
            ps1.setObject(3, livre.getNbPages());
            ps1.setInt(4, livre.getDatePublication());
            ps1.setDouble(5, livre.getPrix());
            ps1.setString(6, livre.getEditeur());
            ps1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        

        try {
            
            PreparedStatement ps2 = Reseau.createStatement("insert into testPOSSEDER values (?, ?, ?)");
            ps2.setInt(1, nouvLibrairie.getId());
            ps2.setString(2, livre.getIsbn());
            ps2.setInt(3, 1);
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return livre;
    }

    public void updateInfoClient(Client client, String nom, String prenom, String adresse, String ville, int codePostal){
        client.setNom(prenom);
        client.setPrenom(prenom);
        client.setAddresse(adresse);
        client.setVille(ville);
        client.setCodePostal(""+codePostal);
    }
}
