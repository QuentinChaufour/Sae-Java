package com.sae_java.Modele;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commande {

    private final int numCommande;
    private Date dateCom;
    private String enLigne;
    private String livraison;
    private int idClient;
    private int idLibrairie;
    private List<DetailCommande> details;

    public Commande(int numCommande, Date date, String enLigne, String livraison, int client, int librairie){
        this.numCommande = numCommande;
        this.dateCom = date;
        this.enLigne = enLigne;
        this.livraison = livraison;
        this.idClient = client;
        this.idLibrairie = librairie;

        this.details = new ArrayList<>();
    }

    /**
     * permet d'obtenir le numéro de commande
     * @return int : le numéro de la commande
     */
    public int getNumCommande(){
        return this.numCommande;
    }

    /**
     * permet d'obtenir la date de la commande
     * @return Date : la date de la commande
     */
    public Date getDate(){
        return this.dateCom;
    }

    /**
     * permet d'obtenir la valeur du champs enLigne
     * @return String "O" pour un commande en ligne,"N" autrement
     */
    public String getEnLigne(){
        return this.enLigne;
    }

    /**
     * permet d'obtenir le type de livraison
     * @return String : "M" pour en magasin, "C" autrement
     */
    public String getLivraison(){
        return this.livraison;
    }

    /**
     * permet d'obtenir l'id du client qui a fait la commande
     * @return int : l'id client
     */
    public int getId(){
        return this.idClient;
    }

    /**
     * permet d'obtenir l'id de la librairie lié a la commande
     * @return int : l'id de la librairie
     */
    public int getIdLibrairie(){
        return this.idLibrairie;
    }

    /**
     * peremt d'obtenir la liste des détails de la commande
     * @return List<DetailCommande> : la liste des détails de la commande
     */
    public List<DetailCommande> getDetails(){
        return new ArrayList<>(this.details);
    }

    /**
     * modifie la date de la commande
     * @param date Date
     */
    public void setDateCom(Date date){
        this.dateCom = date;
    }

    /**
     * modifie le status enLigne de la commande
     * @param enLigne  String
     */
    public void setEnLigne(String enLigne){
        this.enLigne = enLigne;
    }

    /**
     * modifie le mode de livraison de la commande
     * @param livraison String : "M" pour en magasin, "C" autrement
     */
    public void setLivraison(String livraison){
        this.livraison = livraison;
    }

    /**
     * modifie le client a l'origine de la commande
     * @param idClient int
     */
    public void setClient(int idClient){
        this.idClient = idClient;
    }

    /**
     * modifie la librairie lié a la commande
     * @param idLibrairie int
     */
    public void setLibrairie(int idLibrairie){
        this.idLibrairie = idLibrairie;
    } 

    /**
     * Ajoute un détail a la commande
     * @param detail DetailCommande
     */
    public void addDetailCommande(DetailCommande detail){
        this.details.add(detail);
    }

    /**
     * Supprime un détail a la commande
     * @param detail DetailCommande
     */
    public void removeDetailCommande(DetailCommande detail){
        this.details.remove(detail);
    }

    /**
     * Calcule le montant total de la commande.
     * 
     * @return double : Le montant total de la commande en euros
     */
    public double getTotalCommande() {
        double total = 0.0;
        for (DetailCommande detail : this.details) {
            total += detail.getQuantite()*detail.getLivre().getPrix();
        }
        return total;
    }

    @Override
    public String toString() {
        return "Commande {" + "numCommande = " + this.numCommande + ", dateCom = " + this.dateCom + ", enLigne = " + this.enLigne + ", livraison = " + this.livraison + ", idClient = " + this.idClient + ", idMag = " + this.idLibrairie + '}';
    }
}
