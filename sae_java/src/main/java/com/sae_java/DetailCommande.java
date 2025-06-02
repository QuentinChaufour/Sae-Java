package com.sae_java;

public class DetailCommande {
    
    private final int numLig;
    private Livre livre;
    private int quantite;

    public DetailCommande(int numLig, Livre livre, int quantite) throws QuantiteInvalideException{
        this.numLig = numLig;
        this.livre = livre;
        this.quantite = quantite;

        if(quantite <= 0){
            throw new QuantiteInvalideException();
        }
    }

    public int getNumLig(){
        return this.numLig;
    }

    public Livre getLivre(){
        return this.livre;
    }

    public int getQuantite(){
        return this.quantite;
    }

    public void setLivre(Livre livre){
        this.livre = livre;
    }

    public void setQuantite(int quantite){
        this.quantite = quantite;
    }


    @Override
    public String toString() {
        return "DetailCommande{" + "numLig=" + numLig + ", livre=" + livre +", quantite=" + quantite +'}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof DetailCommande)) return false;
        DetailCommande other = (DetailCommande) obj;
        return numLig == other.numLig && livre.equals(other.livre) && quantite == other.quantite;
    }

    @Override
    public int hashCode() {
       return 31*numLig + livre.hashCode() + 19*quantite;
    }
}
