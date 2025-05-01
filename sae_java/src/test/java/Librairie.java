import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Librairie {

    private final int id;
    private String nom;
    private String Ville;

    private Map<Livre,Integer> livreseEnStock = new HashMap<>();
    private List<Livre> livresDisponibles = new ArrayList<>(); 

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
        this.livresDisponibles = new ArrayList<>();
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
     * getteur de la liste des livres disponibles dans la librairie
     * 
     * @return
     */
    public List<Livre> getLivresDisponibles() {
        return new ArrayList<>(this.livresDisponibles);
    }

    /**
     * permet d'ajouter un livre à la librairie en une certaine quantité
     * @param livre : Livre
     * @param quantite : int
     */
    public void ajouterLivre(Livre livre, int quantite) throws QuantiteInvalideException {
        if (quantite <= 0) {
           throw new QuantiteInvalideException();
        }

        if (!this.livresDisponibles.contains(livre)) {
            this.livresDisponibles.add(livre);
        }

        this.livreseEnStock.put(livre, this.livreseEnStock.getOrDefault(livre, 0) + quantite);
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
