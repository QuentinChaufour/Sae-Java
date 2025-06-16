package com.sae_java.Modele;

public abstract class Personne {
 
    private String nom;
    private String prenom;
    private String motDePasse;

    public Personne(String nom, String prenom, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.motDePasse = motDePasse;
    }
    /**
     *getteurs du nom de la personne
     * @return le nom de la personne
     */
    public String getNom() {
        return this.nom;
    }

    /**
     *getteurs du prenom de la personne
     * @return le prenom de la personne
     */
    public String getPrenom() {
        return this.prenom;
    }

    /**
     *getteurs du mot de passe de la personne
     * @return le mot de passe de la personne
     */
    public String getMotDePasse() {
        return this.motDePasse;
    }

    /**
     * setteur du nom de la personne
     * @param nom : String
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * setteur du mot de passe de la personne
     * @param motDePasse : String
     */
    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    /**
     * setteur du prenom de la personne
     * @param prenom : String
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     *permet l'affichage de la personne
     */
    @Override
    public String toString() {
        return "Nom: " + this.nom + ", Prénom: " + this.prenom;
    }

}
