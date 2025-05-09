
public class DetailCommande {
    private int numlig;
    private Livre livre;
    private int qte;

    public DetailCommande(int numlig, Livre livre, int qte){
        this.numlig = numlig;
        this.livre = livre;
        this.qte = qte;
    }

    public int getDetailCommande(){
        return this.numlig;
    }

    public Livre getLivre(){
        return this.livre;
    }

    public int getQte(){
        return this.qte;
    }
}
