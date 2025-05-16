import java.sql.PreparedStatement;
import java.util.List;

public class Vendeur extends Personne{
    private int idVendeur;
    private int id;

    public Vendeur(String nom, String prenom, int id){
        super(nom, prenom);
        this.idVendeur = id;
    }

    public void ajouteLivreStock(Livre livre){
        
    }

    public boolean checkQte(Commande commande){
        for(DetailCommande detail : commande.getDetailCommandes()){
            reseau.getLibrairie(commande.getIdMag()).updateStocks();
            if(!detail.getLivre().checkStocks(detail.getLivre(), detail.getQte())){
                return false;
            }
        }
        return true;
    }

    public List<Commande> preparerCommandes(){
        //transfer panier
    }

    public void enregistrerCommandes(List<Commande> commandes){
        // va faire l'insert des commandes dans la bd et soustraire les stocks
    }

    public void passerCommande(Client client, boolean faireFacture){
        List<Commande> commandesClient = reseau.createCommande(client.getPanier());
        for(Commande commande : commandesClient){
            reseau.enregistrerCommandeBD(commande);
        }
        List<DetailCommande> detailCommandesClient = this.get.createDetailCommande(client.getPanier().getContenu());
        for(DetailCommande detailCommande : detailCommandesClient){
            reseau.enregistrerDetailCommandeBD(detailCommande);
        }
        client.getLibrairie().updateStocks();
        if(faireFacture){
            ProduireFacture(commandesClient);
        }
    }

    public void produireFacture(List<Commande> listeCommandes){
        System.out.println("---------------------------Facture---------------------------");
        for(Commande commande : listeCommandes){
            System.out.println("Commande N°" + commande.getNumCommande());

        }
    }

    public Livre transfererLivre(Livre livre, Librairie nouvLibrairie){
        
    }

    public void updateInfoClient(Client client, String nom, String prenom, String adresse, String ville, int codePostal){
        client.setNom(prenom);
        client.setPrenom(prenom);
        client.setAddresse(adresse);
        client.setVille(ville);
        client.setCodePostal(codePostal);
    }
}
