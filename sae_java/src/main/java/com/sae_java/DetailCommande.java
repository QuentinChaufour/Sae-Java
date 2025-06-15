package com.sae_java;

import com.sae_java.Exceptions.QuantiteInvalideException;

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

    /**
     * permet d'obtenir le numéro du détail
     * @return int
     */
    public int getNumLig(){
        return this.numLig;
    }

    /**
     * permet d'obtenir le livre lié au détail
     * @return Livre
     */
    public Livre getLivre(){
        return this.livre;
    }

    /**
     * permet d'obtenir la quantité commandé
     * @return int
     */
    public int getQuantite(){
        return this.quantite;
    }

    /**
     * permet de modifier le livre
     * @param livre : Livre
     */
    public void setLivre(Livre livre){
        this.livre = livre;
    }

    /**
     * permet de modifier la quantité commandé
     * @param quantite : int
     */
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
