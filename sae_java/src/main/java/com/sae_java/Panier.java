package com.sae_java;

import java.util.HashMap;
import java.util.Map;

public class Panier {

    private final Map<Integer,Map<Livre,Integer>> contenu; // Livre et sa quantité
    private double prixTotal;

    public Panier() {
        this.contenu = new HashMap<>();
        this.prixTotal = 0.0;
    }

    public void ajouterLivre(Livre livre,int idLibrairie,int qte) {

        if(qte <= 0) {
            System.out.println("La quantité doit être supérieure a 0");
            return;
        }

        // si livre existe déjà dans le panier
        if(this.contenu.containsKey(idLibrairie)){
            if(this.contenu.get(idLibrairie).containsKey(livre)){
                this.contenu.get(idLibrairie).put(livre,this.contenu.get(idLibrairie).getOrDefault(livre, 0) + qte); // Met à jour la quantité si existant
            }
            else {
                this.contenu.get(idLibrairie).put(livre, qte); // Ajoute le livre avec sa quantité
            }
        } 
        else {
            this.contenu.put(idLibrairie, new HashMap<>());
            this.contenu.get(idLibrairie).put(livre, qte); 
        }

        this.prixTotal += livre.getPrix() * qte;
    }

    public void retirerLivre(Livre livre, int idLibrairie, int qte) throws PasAssezDeStockException {
       
        if(this.contenu.containsKey(idLibrairie)){
            if(this.contenu.get(idLibrairie).containsKey(livre)){
                int quantite = this.contenu.get(idLibrairie).get(livre);
                if (quantite > qte) {
                    this.contenu.get(idLibrairie).put(livre,this.contenu.get(idLibrairie).get(livre) - qte); // Met à jour la quantité // Met à jour la quantité
                    this.prixTotal -= livre.getPrix() * qte; 
                } 
                else if (quantite == qte) {
                    this.contenu.get(idLibrairie).remove(livre); // Supprime le livre du panier
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
            System.out.println("Le panier n'a pas de livre provenant de la librairie : " + idLibrairie);
        }
    }

    public Map<Integer,Map<Livre,Integer>> getContenu() {
        return new HashMap<>(contenu); // Retourne une copie pour éviter les modifications externes
    }


    public double getPrixTotal() {
        return this.prixTotal;
    }

    public void viderPanier() {
        this.contenu.clear();
        this.prixTotal = 0.0;
    }


    @Override
    public String toString() {
        String panier = "Contenu du panier :\n";
        for (int idLib : contenu.keySet()) {
            try {
                panier += "Librairie : " + Reseau.getLibrairie(idLib).getNom() + "\n";
            } catch (LibraryNotFoundException e) {
                continue;
            }
            panier += "--------------------------------------\n";
            for(Livre livre : contenu.get(idLib).keySet()) {
                panier += livre.toString() + "\n";
                panier += "Quantité : " + contenu.get(idLib).get(livre) + "\n";
            }
        }
        panier += "Prix total : " + this.prixTotal + " euro(s)";
        return panier;
    }
}
