import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Commande {

    private int numCommande;
    private String dateCom;
    private String enLigne;
    private String livraison;
    private int idClient;
    private int idMag;

    public Commande(int numCommande, String dateCom, String enLigne, String livraison, int idClient, int idMag){
        this.numCommande = numCommande;
        this.dateCom = dateCom;
        this.enLigne = enLigne;
        this.livraison = livraison;
        this.idClient = idClient;
        this.idMag = idMag;
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
        return this.idClient;
    }

    public int getLibrairie(){
        return this.idMag;
    }

    public List<Object> getDetail(){
        return Arrays.asList(this.getNumCommande(),this.getDate(),this.getLivraison(),this.getId(),this.getLibrairie());

    }

    public void setNumCommande(int numCom){
        this.numCommande = numCom;
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

    public void setIdClient(int id){
        this.idClient = id;
    }

    public void setIdMag(int id){
        this.idMag = id;
    } 

    
    

}
