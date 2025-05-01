public class Auteur{
    
    private int idAuteur;
    private String nomPrenom;
    private Integer dteNaissance;
    private Integer dteMort;

    /**
     * Constructeur de la classe Auteur
     * @param id : int
     * @param nomPrenom : String
     */
    public Auteur(int id, String nomPrenom,Integer dteNaissance, Integer dteMort) {
        this.idAuteur = id;
        this.nomPrenom = nomPrenom;  
        this.dteNaissance = dteNaissance;
        this.dteMort = dteMort;
    }
    
    /**
     * getteur de l'id de l'auteur
     * 
     * @return l'id de l'auteur : int
     */
    public int getId() {
        return idAuteur;
    }

    /**
     * Getteur du nom et prénom de l'auteur
     * 
     * @return le nom et prénom de l'auteur : String
     */
    public String getNomPrenom() {
        return nomPrenom;
    }

    /**
     * getteur de la date de naissance de l'auteur
     * 
     * @return la date de naissance de l'auteur : Integer
     */
    public Integer getDteNaissance() {
        return dteNaissance;
    }


    /**
     * getteur de la date de mort de l'auteur
     * 
     * @return la date de mort de l'auteur : Integer
     */
    public Integer getDteMort() {
        return dteMort;
    }

    /**
     * setteur du nom et prénom de l'auteur
     * 
     * @param nomPrenom
     */
    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    /**
     * setteur de la date de naissance de l'auteur
     * 
     * @param dteNaissance : Integer
     */
    public void setDteNaissance(Integer dteNaissance) {
        this.dteNaissance = dteNaissance;
    }

    /**
     * setteur de la date de mort de l'auteur
     * 
     * @param dteMort : Integer
     */
    public void setDteMort(Integer dteMort) {
        this.dteMort = dteMort;
    }

    /**
     * permey de d'afficher l'auteur
     */
    @Override
    public String toString() {
        return this.nomPrenom;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {return false;}
        if (obj == this) {return true;}

        if (!(obj instanceof Auteur)) {return false;}
        Auteur auteur = (Auteur) obj;
        return idAuteur == auteur.idAuteur;
    }
}
