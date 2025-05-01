public class Librairie {

    private final int id;
    private String nom;
    private String Ville;

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
}
