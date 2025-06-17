package com.sae_java.Modele;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sae_java.Modele.Enumerations.EnumUpdatesDB;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;

public class Vendeur extends Personne{
    private int idVendeur;
    private int idlibrairie;

    public Vendeur(String nom, String prenom, String motDePasse, int id, int idlibrairie){
        super(nom, prenom, motDePasse);
        this.idVendeur = id;
        this.idlibrairie = idlibrairie;
    }

    public int getIdLibrairie() {
        return this.idlibrairie;
    }

    public int getIdVendeur() {
        return this.idVendeur;
    }

//    public void ajouteLivreStock(Livre livre){
//        
//        Connection connection = Reseau.getConnection();
//        PreparedStatement ps;
//        try {
//            ps = connection.prepareStatement("insert into LIVRE values (?,?,?,?,?,?,?,?)");
//            ps.setString(1, livre.getIsbn());
//            ps.setString(2, livre.getTitre());
//            ps.setObject(3, livre.getAuteurs());
//            ps.setString(4, livre.getEditeur());
//            ps.setInt(5, livre.getDatePublication());
//            ps.setDouble(6, livre.getPrix());
//            ps.setInt(7, livre.getNbPages());
//            ps.setString(8, livre.getClassification());
//            ps.executeUpdate();
//            Reseau.updateInfos(EnumUpdatesDB.STOCKS);
//        } catch (SQLException e) {
//            e.printStackTrace();    
//        } 
//    }


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

    public List<Commande> preparerCommandes(int idClient, Map<Livre, Integer> listeLivres, Date date, String enLigne, String Livraison ){
        List<Commande> listeCommandes = new ArrayList<>();
        listeCommandes.add(new Commande(Reseau.numCom, date, enLigne, Livraison,idClient,this.idlibrairie)); // Cf Client pour la création de la commande avec le numCom
        Panier panier = createPanier(listeLivres);
        for(Livre livre : listeLivres.keySet()){
            
        }
        return null;
    }

    public void enregistrerCommandes(List<Commande> commandes){
        // va faire l'insert des commandes dans la bd et soustraire les stocks
        for(Commande commande : commandes){
            Reseau.enregisterCommande(commande);
            for(DetailCommande detail : commande.getDetails()){
                //Reseau.enregistrerDetailCommandeBD(detail);
            }
        Reseau.updateInfos(EnumUpdatesDB.STOCKS);
        }
    }
/* 
    public void passerCommande(Client client, boolean faireFacture){
        List<Commande> commandesClient = Reseau.createCommande(client.getPanier());
        for(Commande commande : commandesClient){
            Reseau.enregistrerCommandeBD(commande);
        }
        List<DetailCommande> detailCommandesClient = this.get.createDetailCommande(client.getPanier().getContenu());
        for(DetailCommande detailCommande : detailCommandesClient){
            reseau.enregistrerDetailCommandeBD(detailCommande);
        }
        client.getLibrairie().updateStocks();
        if(faireFacture){
            ProduireFacture(commandesClient);
        }
    }

    public void produireFacture(List<Commande> listeCommandes){
        System.out.println("---------------------------Facture---------------------------");
        for(Commande commande : listeCommandes){
            System.out.println("Commande N°" + commande.getNumCommande());

        }
    }

    public Livre transfererLivre(Livre livre, Librairie nouvLibrairie){
        Connection connection = Reseau.getConnection();
        PreparedStatement ps1;
        try {
            ps1 = connection.prepareStatement("insert into LIVRE values (?, ?, ?, ?, ?, ?, ?, ?)");
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
        
        PreparedStatement ps2;
        try {
            ps2 = connection.prepareStatement("insert into POSSEDER values (?, ?, ?)");
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
        client.setCodePostal(codePostal);
    }
        */
}
