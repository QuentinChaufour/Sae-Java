package com.sae_java;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Librairie implements Comparable<Librairie>{

    private final int id;
    private String nom;
    private String Ville;

    private Map<Livre,Integer> livreseEnStock = new HashMap<>();

    /**
     * Constructeur de la classe Librairie
     * @param id : int
     * @param nom : String
     * @param ville : String
     */
    public Librairie(int id,String nom, String ville) {
        this.id = id;
        this.nom = nom;
        this.Ville = ville;

        this.livreseEnStock = new HashMap<>();
    }

    /**
     * getteur de l'id de la librairie
     * 
     * @return l'id de la librairie : int
     */
    public int getId() {
        return id;
    }

    /**
     * getteur du nom de la librairie
     * 
     * @return le nom de la librairie : String
     */
    public String getNom() {
        return nom;
    }

    /**
     * getteur de la ville de la librairie
     * 
     * @return la ville de la librairie : String
     */
    public String getVille() {
        return Ville;
    }

    /**
     * setteur du nom de la librairie
     * 
     * @param nom : String
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * setteur de la ville de la librairie
     * 
     * @param ville : String
     */
    public void setVille(String ville) {
        this.Ville = ville; 
    }

    /**
     * getteur des stocks de livres de la librairie
     * 
     * @return le stock de livres de la librairie : Map<Livre,Integer>
     */
    public Map<Livre,Integer> consulterStock() {
        return new HashMap<>(this.livreseEnStock);
    }

    /**
     * permet d'ajouter un livre au stock de la librairie dans le cadre de la mise a jour des stocks
     * 
     * @param livre : Livre
     * @param quantite : int
     */
    public void ajouterAuStock(Livre livre, int quantite) throws QuantiteInvalideException {
        if (quantite <= 0) {
            throw new QuantiteInvalideException();
        }
        this.livreseEnStock.put(livre, this.livreseEnStock.getOrDefault(livre, 0) + quantite);
    }

    /**
     * permet d'ajouter un livre existant ou nouveau à la librairie en une certaine quantité ainsi que dans la BD
     * @param livre : Livre
     * @param quantite : int
     */
    public void ajouterNouveauLivre(Livre livre, int quantite) throws QuantiteInvalideException, SQLException {
        if (quantite <= 0) {
           throw new QuantiteInvalideException();
        }

        if(this.livreseEnStock.containsKey(livre)){
            //mettre a jour la qte

            PreparedStatement statement = Reseau.createStatement("UPDATE testPOSSEDER SET qte = qte + ? WHERE isbn = ? AND idmag = ?");
            statement.setInt(1, quantite);
            statement.setString(2, livre.getIsbn());
            statement.setInt(3, this.id);
            statement.executeUpdate();
            statement.close();
            // mettre à jour le stock de la librairie
            this.ajouterAuStock(livre, quantite);

        }
        else if(this.checkBookInDB(livre)) {
            
            // livre existe déjà dans la base de données, on ajoute l'association avec la librairie

            PreparedStatement statement = Reseau.createStatement("INSERT INTO testPOSSEDER VALUES (?, ?, ?)");
            statement.setInt(1, this.id);
            statement.setString(2, livre.getIsbn());
            statement.setInt(3, quantite);
            statement.executeUpdate();
            statement.close();
            // mettre à jour le stock de la librairie
            this.ajouterAuStock(livre, quantite);
        }
        else{
            // livre n'existe pas dans la base de données, on l'ajoute et on ajoute l'association avec la librairie

            this.ajouterLivreDansBD(livre);
            PreparedStatement statement = Reseau.createStatement("INSERT INTO testPOSSEDER VALUES (?, ?, ?)");
            statement.setInt(1, this.id);
            statement.setString(2, livre.getIsbn());
            statement.setInt(3, quantite);
            statement.executeUpdate();
            statement.close();

            // mettre à jour le stock de la librairie
            this.ajouterAuStock(livre, quantite);
        }
    }

    /**
     * permet de retirer un livre de la librairie en une certaine quantité et de mettre à jour la base de données
     * @param livre : Livre
     * @param quantite : int
     */
    public void retirerLivre(Livre livre,int quantite) throws QuantiteInvalideException , BookNotInStockException, SQLException {
        
        if (this.livreseEnStock.containsKey(livre)) {

            int qte = this.livreseEnStock.get(livre);
            if (qte > quantite) {
                this.livreseEnStock.put(livre, qte - quantite);

                // mettre à jour la quantité dans la base de données
                PreparedStatement statement = Reseau.createStatement("UPDATE testPOSSEDER SET qte = qte - ? WHERE isbn = ? AND idmag = ?");
                statement.setInt(1, quantite);
                statement.setString(2, livre.getIsbn());
                statement.setInt(3, this.id);
                statement.executeUpdate();
                statement.close();
            } 
            else if (qte == quantite) {
                this.livreseEnStock.remove(livre);
                // supprimer le livre de la base de données
                PreparedStatement statement = Reseau.createStatement("DELETE FROM testPOSSEDER WHERE isbn = ? AND idmag = ?");
                statement.setString(1, livre.getIsbn());
                statement.setInt(2, this.id);
                statement.executeUpdate();
                statement.close();
            } 
            else {
                throw new QuantiteInvalideException();
            }
        } 
        else {
           throw new BookNotInStockException();
        }
    }

    /**
     * verifie si un livre est présent en une certaine quantité dans les stocks du magasin
     * 
     * @param livre : Livre
     * @param qte : int
     * @return
     */
    public boolean checkStock(Livre livre,int qte){

        if(this.livreseEnStock.containsKey(livre)){
            return this.livreseEnStock.get(livre) >= qte;
        }
        else{
            return false;
        }

    }

    /**
     * vérifie si un livre est présent dans la base de données
     * @param livre
     * @return le livre existe déja dans la base de données : boolean
     */
    private boolean checkBookInDB(Livre livre) throws SQLException {

        PreparedStatement statement = Reseau.createStatement("SELECT * FROM testLIVRE WHERE isbn = ?");
        statement.setString(1, livre.getIsbn());
        ResultSet resultSet = statement.executeQuery();
        boolean isIn = resultSet.next();
        statement.close();
        return isIn;
    }


    /**
     * ajoute un livre dans la base de données et ajoute les auteurs et leurs liens avec le livre
     * @param livre : Livre
     */
    private void ajouterLivreDansBD(Livre livre) throws SQLException {

        PreparedStatement statementLivre = Reseau.createStatement("INSERT INTO testLIVRE VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        statementLivre.setString(1, livre.getIsbn());
        statementLivre.setString(2, livre.getTitre());
        statementLivre.setInt(3, livre.getNbPages());
        statementLivre.setInt(4, livre.getDatePublication());
        statementLivre.setBigDecimal(5, new BigDecimal(livre.getPrix()));
        statementLivre.setString(6, livre.getClassification());
        statementLivre.setString(7, livre.getEditeur());
        statementLivre.setBinaryStream(8, null); // no image for now in terminal
        statementLivre.executeUpdate();
        statementLivre.close();

        //ajouter les auteurs et leurs liens avec le livre
        for(Auteur auteur : livre.getAuteurs()) {
            if (!this.checkAuteurInBD(auteur)) {
                PreparedStatement statementAuteur = Reseau.createStatement("INSERT INTO testAUTEUR VALUES (?, ?, ?, ?)");
                statementAuteur.setString(1, auteur.getId());
                statementAuteur.setString(2, auteur.getNomPrenom());

                if(auteur.getDteNaissance() == null) {
                    statementAuteur.setNull(3, java.sql.Types.INTEGER);
                } else {
                    statementAuteur.setInt(3, auteur.getDteNaissance());
                }

                if(auteur.getDteMort() == null) {
                    statementAuteur.setNull(4, java.sql.Types.INTEGER);
                } else {
                    statementAuteur.setInt(4, auteur.getDteMort());
                }

                statementAuteur.executeUpdate();
                statementAuteur.close();
            }

            PreparedStatement statementEcrire = Reseau.createStatement("INSERT INTO testECRIRE VALUES (?, ?)");
            statementEcrire.setString(1, livre.getIsbn());
            statementEcrire.setString(2, auteur.getId());
            statementEcrire.executeUpdate();
            statementEcrire.close();
        }

    }

    /**
     * vérifie si un auteur est présent dans la base de données
     * @param auteur : Auteur
     * @return l'auteur existe déja dans la base de données : boolean
     */
    private boolean checkAuteurInBD(Auteur auteur) throws SQLException {

        PreparedStatement statement = Reseau.createStatement("SELECT * FROM testAUTEUR WHERE idauteur = ?");
        statement.setString(1, auteur.getId());
        ResultSet resultSet = statement.executeQuery();
        boolean exists = resultSet.next();
        statement.close();
        return exists;
    }

    /**
     * permet l'affichage de la librairie
     */
    @Override
    public String toString() {
        return "la librairie " + nom + " est située à " + Ville + " et a pour id " + id;
    }

    /**
     * permet de tester l'égalité deux librairies selon leurs id
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Librairie)) return false;

        Librairie librairie = (Librairie) obj;

        return this.id == librairie.id;
    }

    @Override
    public int hashCode(){
        int hash = 0;
        hash += this.id * 31;
        hash += this.nom.hashCode() * 19;
        hash += this.Ville.hashCode() * 3;

        return hash;
    }

    @Override
    public int compareTo(Librairie lib){

        if(this.id == lib.id){return  0;}
        else if(this.id > lib.id){return 1;}
        else{return  -1;}
    }
}
