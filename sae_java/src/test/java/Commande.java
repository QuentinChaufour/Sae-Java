import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Commande {
    private int NumCommande;
    private String dateCom;
    private String enLigne;
    private String livraison;
    private int idClient;
    private int idMag;
    private List<DetailCommande> listesDetailsCommandes;

    public Commande(int NumCommande, String dateCom, String enLigne, String livraison, int idClient, int idMag, Map<Livre,Integer> listeLivres){
        this.NumCommande = NumCommande;
        this.dateCom = dateCom;
        this.enLigne = enLigne;
        this.livraison = livraison;
        this.idClient = idClient;
        this.idMag = idMag;
        for(Livre livre : listeLivres.keySet()){
            reseau.numlig++;
            this.listesDetailsCommandes.add(new DetailCommande(reseau.numlig, livre, listeLivres.get(livre)));
            
        }
    }


    public int getNumCommande() {
        return this.NumCommande;
    }
    
    public String getDateCom() {
        return this.dateCom;
    }
    
    public String getEnLigne() {
        return this.enLigne;
    }
    
    public String getLivraison() {
        return this.livraison;
    }
    
    public int getIdClient() {
        return this.idClient;
    }
    
    public int getIdMag() {
        return this.idMag;
    }

    public List<DetailCommande> getDetailCommandes(){
        return this.listesDetailsCommandes;
    }
}
