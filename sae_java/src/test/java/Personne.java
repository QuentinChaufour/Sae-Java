public abstract class Personne {
 
    private String nom;
    private String prenom;

    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }
    /**
     *getteurs du nom de la personne
     * @return le nom de la personne
     */
    public String getNom() {
        return nom;
    }

    /**
     *getteurs du prenom de la personne
     * @return le prenom de la personne
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * setteur du nom de la personne
     * @param nom : String
     */
    public void setNom(String nom) {
        this.nom = nom;
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
        return "Nom: " + nom + ", Prénom: " + prenom;
    }

}
