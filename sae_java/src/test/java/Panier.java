import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Panier {

    private Map<Librairie,Map<Livre,Integer>> contenu; // Livre et sa quantité
    private List<Livre> livres; // Liste des livres ordonnée pour selection
    private double prixTotal;

    public Panier() {
        this.contenu = new HashMap<>();
        this.livres = new ArrayList<>();
        this.prixTotal = 0.0;
    }

    public void ajouterLivre(Livre livre, Librairie librairie,int qte) {

        if(qte <= 0) {
            System.out.println("La quantité doit être supérieure a 0");
            return;
        }

        // si livre existe déjà dans le panier
        if(this.contenu.containsKey(librairie)){
            if(this.contenu.get(librairie).containsKey(livre)){
                Integer quantite = this.contenu.get(librairie).get(livre);
                quantite += qte; // Incrémente la quantité
                this.contenu.get(librairie).put(livre, quantite); // Met à jour la quantité
            }
            else {
                this.contenu.get(librairie).put(livre, qte); // Ajoute le livre avec sa quantité
                this.livres.add(livre); // Ajoute le livre à la liste
            }
        } 
        else {
            this.contenu.put(librairie, new HashMap<>());
            this.contenu.get(librairie).put(livre, qte);
            this.livres.add(livre); 
        }

        this.prixTotal += livre.getPrix() * qte;
    }

    public void retirerLivre(Livre livre, Librairie librairie, int qte) throws PasAssezDeStockException {
       
        if(this.contenu.containsKey(librairie)){
            if(this.contenu.get(librairie).containsKey(livre)){
                int quantite = this.contenu.get(librairie).get(livre);
                if (quantite > qte) {
                    quantite -= qte; // Décrémente la quantité
                    this.contenu.get(librairie).put(livre, quantite); // Met à jour la quantité
                    this.prixTotal -= livre.getPrix() * qte; 
                } 
                else if (quantite == qte) {
                    this.contenu.get(librairie).remove(livre); // Supprime le livre du panier
                    this.livres.remove(livre);
                    this.prixTotal -= livre.getPrix() * qte; 
                }

                else {
                    throw new PasAssezDeStockException();
                } 
            }
            else {
                System.out.println("Le livre " + livre + " n'est pas dans le panier.");
            }
            
        } 
        else {
            System.out.println("Le panier n'a pas de livre de la librairie : " + librairie);
        }
    }

    public Map<Librairie,Map<Livre,Integer>> getContenu() {
        return new HashMap<>(contenu); // Retourne une copie pour éviter les modifications externes
    }

    public List<Livre> getLivres() {
        return new ArrayList<>(livres); // Cf. ci-dessus
    }

    public double getPrixTotal() {
        return this.prixTotal;
    }

    public void viderPanier() {
        this.contenu.clear();
        this.livres.clear();
        this.prixTotal = 0.0;
    }


    @Override
    public String toString() {
        String panier = "Contenu du panier :\n";
        for (Librairie librairie : contenu.keySet()) {
            panier += "Librairie : " + librairie.getNom() + "\n";
            panier += "--------------------------------------\n";
            for(Livre livre : contenu.get(librairie).keySet()) {
                panier += livre.toString() + "\n";
                panier += "Quantité : " + contenu.get(librairie).get(livre) + "\n";
            }
        }
        panier += "Prix total : " + this.prixTotal + " €";
        return panier;
    }
}
