package com.sae_java.Modele;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.Image;

public class Livre implements Comparable<Livre> {

    private final String isbn;
    private String titre;
    private List<Auteur> auteurs;
    private String editeur;
    private Integer datePublication;
    private double prix;
    private int nbPages;
    private String classification;
    private Image image;

    public Livre(String isbn, String titre, List<Auteur> auteurs, String editeur, Integer datePublication, double prix, int nbPages, String classification) {
        this.isbn = isbn;
        this.titre = titre;
        this.auteurs = auteurs;
        this.editeur = editeur;
        this.datePublication = datePublication;
        this.prix = prix;
        this.nbPages = nbPages;
        this.classification = classification;
    }

    public Livre(String isbn, String titre, String editeur, int datePublication, double prix, int nbPages, String classification,Image img) {
        this.isbn = isbn;
        this.titre = titre;
        this.auteurs = new ArrayList<>();
        this.editeur = editeur;
        this.datePublication = datePublication;
        this.prix = prix;
        this.nbPages = nbPages;
        this.classification = classification;
        this.image = img;
    }

    /**
     * getteur de l'ISBN du livre
     * 
     * @return l'ISBN du livre : String
     */
    public String getIsbn(){
        return this.isbn;
    }

    /**
     * getteur du titre du livre
     * 
     * @return le titre du livre : String
     */
    public String getTitre(){
        return this.titre;
    }

    /**
     * getteur de la liste des auteurs du livre
     * 
     * @return la liste des auteurs du livre : List<Auteur>
     */
    public List<Auteur> getAuteurs(){
        return new ArrayList<>(this.auteurs);
    }

    /**
     * getteur de l'éditeur du livre
     * 
     * @return l'éditeur du livre : String
     */
    public String getEditeur(){
        return this.editeur;
    }

    /**
     * getteur de la date de publication du livre
     * 
     * @return la date de publication du livre : Integer
     */
    public int getDatePublication(){
        return this.datePublication;
    }

    /**
     * getteur du prix du livre
     * 
     * @return le prix du livre : double
     */
    public double getPrix(){
        return this.prix;
    }

    /**
     * getteur du nombre de pages du livre
     * 
     * @return le nombre de pages du livre : int
     */
    public int getNbPages(){
        return this.nbPages;
    }

    /**
     * getteur de la classification du livre
     * 
     * @return la classification du livre : String
     */
    public String getClassification(){
        return this.classification;
    }

    /**
     * modifie le prix du livre
     * 
     * @param prix : double
     */
    public void setPrix(double prix){
        this.prix = prix;
    }

    /**
     * modifie le titre du livre
     * 
     * @param titre : String
     */
    public void setTitre(String titre){
        this.titre = titre;
    }

    /**
     * modifie l'auteur du livre
     * @param editeur : String
     */
    public void setEditeur(String editeur){
        this.editeur = editeur;
    }

    /**
     * modifie la date de publication du livre
     * @param datePublication : Integer
     */
    public void setDatePublication(Integer datePublication){
        this.datePublication = datePublication;
    }

    /**
     * modifie le nombre de pages du livre
     * @param nbPages : int
     */
    public void setNbPages(int nbPages){
        this.nbPages = nbPages;
    }

    /**
     * modifie la classification du livre
     * @param classification : String
     */
    public void setClassification(String classification){
        this.classification = classification;
    }

    /**
     * permet d'ajouter un auteur au livre
     * @param auteur : Auteur
     */
    public void ajouterAuteur(Auteur auteur) {
        this.auteurs.add(auteur);
    }

    public Image getImage(){
        return this.image;
    }

    public void setImage(Image img){
        this.image = img;
    }

    /**
     * permet l'affichage de l'ensemble des infos du livre
     * @return String
     */
    public String completeDisplay(){
        return this.titre + " de " + this.auteurs + ", " + this.editeur + ", " + this.datePublication + ", " + this.prix + " euros, " + this.nbPages + " pages, " + this.classification;
    }

    /**
     * permet l'affichage du livre
     */
    @Override
    public String toString() {
        return this.titre +  ", " + this.prix + " euros";
    }

    /**
     * permet de comparer deux livres
     * @return true si les livres sont égaux, false sinon : boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Livre)) return false;

        Livre livre = (Livre) obj;

        return isbn.equals(livre.isbn);
    }

    @Override
    public int hashCode() {

        return 31 * isbn.hashCode();
    }

    @Override
    public int compareTo(Livre o) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.isbn, o.isbn);
    }
}