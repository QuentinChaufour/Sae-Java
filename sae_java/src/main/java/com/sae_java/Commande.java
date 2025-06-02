package com.sae_java;

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

    public int getNumCommande(){
        return this.numCommande;
    }

    public Date getDate(){
        return this.dateCom;
    }

    public String getEnLigne(){
        return this.enLigne;
    }

    public String getLivraison(){
        return this.livraison;
    }

    public int getId(){
        return this.idClient;
    }

    public int getIdLibrairie(){
        return this.idLibrairie;
    }

    public List<DetailCommande> getDetails(){
        return new ArrayList<>(this.details);
    }

    public void setDateCom(Date date){
        this.dateCom = date;
    }

    public void setEnLigne(String enLignee){
        this.enLigne = enLignee;
    }

    public void setLivraison(String livraison){
        this.livraison = livraison;
    }

    public void setClient(int idClient){
        this.idClient = idClient;
    }

    public void setLibrairie(int idLibrairie){
        this.idLibrairie = idLibrairie;
    } 

    public void addDetailCommande(DetailCommande detail){
        this.details.add(detail);
    }

    public void removeDetailCommande(DetailCommande detail){
        this.details.remove(detail);
    }

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
