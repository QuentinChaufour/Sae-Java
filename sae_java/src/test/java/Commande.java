import java.util.ArrayList;
import java.util.List;

public class Commande {

    private final int numCommande;
    private String dateCom;
    private String enLigne;
    private String livraison;
    private Client client;
    private Librairie librairie;
    private List<DetailCommande> details;

    public Commande(int numCommande, String dateCom, String enLigne, String livraison, Client client, Librairie librairie){
        this.numCommande = numCommande;
        this.dateCom = dateCom;
        this.enLigne = enLigne;
        this.livraison = livraison;
        this.client = client;
        this.librairie = librairie;

        this.details = new ArrayList<>();
    }

    public int getNumCommande(){
        return this.numCommande;
    }

    public String getDate(){
        return this.dateCom;
    }

    public String getEnLigne(){
        return this.enLigne;
    }

    public String getLivraison(){
        return this.livraison;
    }

    public int getId(){
        return this.client.getId();
    }

    public int getIdLibrairie(){
        return this.librairie.getId();
    }

    public List<DetailCommande> getDetails(){
        return new ArrayList<>(this.details);
    }

    public void setDateCom(String date){
        this.dateCom = date;
    }

    public void setEnLigne(String enLignee){
        this.enLigne = enLignee;
    }

    public void setLivraison(String livraison){
        this.livraison = livraison;
    }

    public void setClient(Client client){
        this.client = client;
    }

    public void setLibrairie(Librairie librairie){
        this.librairie = librairie;
    } 

    public void addDetailCommande(DetailCommande detail){
        this.details.add(detail);
    }

    public void removeDetailCommande(DetailCommande detail){
        this.details.remove(detail);
    }

    @Override
    public String toString() {
        return "Commande {" + "numCommande = " + this.numCommande + ", dateCom = " + this.dateCom + ", enLigne = " + this.enLigne + ", livraison = " + this.livraison + ", idClient = " + this.client.getId() + ", idMag = " + this.librairie.getId() + '}';
    }
}
