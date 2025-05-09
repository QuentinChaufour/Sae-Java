
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestSAE {
    

    @Test
    public void testClientBasics() {
        Client client = new Client("Julie", "Martin", 3, "133 boulevard de l''Université", "45000", "Orléans",new Librairie(7,"Loire et livres", "Orléans"));

        // Test getters
        assertEquals("Julie", client.getNom());
        assertEquals("Martin", client.getPrenom());
        assertEquals(3, client.getId());
        assertEquals("133 boulevard de l''Université", client.getAddress());
        assertEquals("45000", client.getCodePostal());
        assertEquals("Orléans", client.getVille());
        assertEquals(new Librairie(7,"Loire et livres", "Orléans"), client.getLibrairie());
        
        client.setLibrairie(new Librairie(1,"La librairie parisienne", "Paris"));
        client.setNom("Dupont");
        client.setPrenom("Jean");
        client.setAddresse("456 avenue de la République");
        client.setCodePostal("75000");
        client.setVille("Paris");

        assertEquals("Dupont", client.getNom());
        assertEquals("Jean", client.getPrenom());
        assertEquals("456 avenue de la République", client.getAddress());
        assertEquals("75000", client.getCodePostal());
        assertEquals("Paris", client.getVille());
        assertEquals(new Librairie(1,"La librairie parisienne", "Paris"), client.getLibrairie());
        // pas de setteur pour idClient car c'est un attribut non modifiable (final)

    }

    @Test
    public void testLibrairieBasics() {
        Librairie librairie = new Librairie(1, "La librairie parisienne", "Paris");

        // Test getters
        assertEquals(1, librairie.getId());
        assertEquals("La librairie parisienne", librairie.getNom());
        assertEquals("Paris", librairie.getVille());

        // Test setters
        librairie.setNom("La librairie de Lyon");
        //pas de setteur pour idLibrairie car c'est un attribut non modifiable (final)
        librairie.setVille("Lyon"); // possible de changer la ville mais inutile car si une librairie est déplacée dans une autre ville, il faut créer une nouvelle librairie sauf erreurs de saisie dans la BD 

        assertEquals("La librairie de Lyon", librairie.getNom());
        assertEquals("Lyon", librairie.getVille());
    }

    @Test
    public void testLivre() {

        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur(1,"H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");

        assertTrue(livre.getIsbn().equals("120"));
        assertEquals("La Guerre des mondes", livre.getTitre());
        assertTrue(livre.getAuteurs().contains(new Auteur(1,"H.G. Wells",null,null)));
        assertEquals("Gallimard", livre.getEditeur());
        assertEquals(1898, livre.getDatePublication());
        assertEquals(9.99, livre.getPrix(), 0.00);
        assertEquals(159, livre.getNbPages());
        assertEquals("Science Fiction", livre.getClassification());

        livre.setPrix(12.99);
        
        assertEquals(12.99, livre.getPrix(), 0.00);
    }

    @Test
    public void testPanier() {

        Panier panier = new Panier();
        Livre livre1 = new Livre("120","La Guerre des mondes",  Arrays.asList(new Auteur(1,"H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");
        Livre livre2 = new Livre("121","Le Petit Prince",  Arrays.asList(new Auteur(2,"Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman");

        Librairie librairie = new Librairie(1, "La librairie parisienne", "Paris");
        // Test ajout de livres
        panier.ajouterLivre(livre1,librairie,1);
        panier.ajouterLivre(livre2,librairie,1);

        Map<Librairie,Map<Livre,Integer>> contenu = new HashMap<>();
        contenu.put(librairie, new HashMap<>());
        contenu.get(librairie).put(livre1, 1);
        contenu.get(librairie).put(livre2, 1);

        assertTrue(panier.getContenu().equals(contenu));
        assertTrue(panier.getLivres().contains(livre1));
        assertTrue(panier.getLivres().contains(livre2));

        assertEquals(17.98, panier.getPrixTotal(), 0.00);

        // Test suppression de livres
        try{
            panier.retirerLivre(livre2,librairie,1);
            assertFalse(panier.getLivres().contains(livre2));
        }catch (PasAssezDeStockException e) {
            System.err.println("Pas assez de stock pour retirer le livre");
        }
        panier.viderPanier();
        assertTrue(panier.getLivres().isEmpty());
        assertTrue(panier.getContenu().isEmpty());
        assertEquals(0.0, panier.getPrixTotal(), 0.00);

    }

    @Test
    public void testPanierClient(){

        Client client = new Client("Julie", "Martin", 3, "133 boulevard de l''Université", "45000", "Orléans",new Librairie(7,"Loire et livres", "Orléans"));

        // Test ajout/suppression de livres au panier du client
        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur(1,"H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");
        client.ajouterAuPanier(livre,client.getLibrairie(), 1);
        assertTrue(client.getPanier().getLivres().contains(livre));
        assertFalse(client.getPanier().getLivres().contains(new Livre("121","Le Petit Prince", Arrays.asList(new Auteur(2,"Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman")));

        try {
            client.retirerDuPanier(livre, client.getLibrairie(), 1);
            assertFalse(client.getPanier().getContenu().get(client.getLibrairie()).containsKey(livre));
        } 
        catch (PasAssezDeStockException e) {
            System.err.println("Pas assez de stock pour retirer le livre");
        }
    }

    @Test
    public void testLibrairieStock() {
        Librairie librairie = new Librairie(1, "La librairie parisienne", "Paris");
        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur(1,"H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");
        Livre livre2 = new Livre("121","Le Petit Prince", Arrays.asList(new Auteur(2,"Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman");

        // Test ajout de livres au stock
        try {
            librairie.ajouterLivre(livre, 5);
            assertTrue(librairie.consulterStock().containsKey(livre));
            assertTrue(librairie.consulterStock().get(livre) == 5);

            librairie.ajouterLivre(livre, 2);
            assertTrue(librairie.consulterStock().get(livre) == 7);

            librairie.retirerLivre(livre,6);
            assertTrue(librairie.consulterStock().get(livre) == 1);

            librairie.retirerLivre(livre,1);
            assertFalse(librairie.consulterStock().containsKey(livre));

            librairie.ajouterLivre(livre2, 0);
        }
        catch (QuantiteInvalideException e) {
            System.err.println("Quantité invalide");
        }
        catch (BookNotInStockException e) {
            System.err.println("Livre non disponible");
        }


        try {
            librairie.ajouterLivre(livre, 5);
        }
        catch (QuantiteInvalideException e) {
            System.err.println("Quantité invalide");
        }

        // Test consultation du stock
        assertTrue(librairie.consulterStock().containsKey(livre));
        assertFalse(librairie.consulterStock().containsKey(livre2));

        assertTrue(librairie.consulterStock().get(livre) == 5);

    }

    @Test
    public void testConsultationClient(){

        Librairie librairie = new Librairie(7,"Loire et livres", "Orléans");
        Client client = new Client("Julie", "Martin", 3, "133 boulevard de l''Université", "45000", "Orléans",librairie);
        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur(1,"H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");

        try{
            librairie.ajouterLivre(livre,3);
        }
        catch (QuantiteInvalideException e) {
            System.err.println("Quantité invalide");
        }

        assertTrue(client.consulterLivres().containsKey(livre));

    }
  
    @Test
    public void testSelectionLivre(){

        Librairie librairie = new Librairie(7, "Loire et livres", "Orléans");
        Client client = new Client("Julie", "Martin", 3, "133 boulevard de l''Université", "45000", "Orléans",librairie);
        Livre livre = new Livre("120", "La Guerre des mondes", Arrays.asList(new Auteur(1, "H.G. Wells", null, null)),"Gallimard", 1898, 9.99, 159, "Science Fiction");


        try{
            librairie.ajouterLivre(livre, 3);
        }
        catch(QuantiteInvalideException e){
            System.out.println("Quantité invalide");
        }

        try{
            client.ajouterAuPanier(client.getLivreFromLibrairie(1,3), librairie, 3);

            assertTrue(client.getPanier().getContenu().get(librairie).containsKey(livre));
            assertTrue(client.getPanier().getContenu().get(librairie).get(livre) == 3);
        }
        catch(PasAssezDeStockException e){
            System.out.println("Pas assez de livre : " + livre + " dans la librairie " + client.getLibrairie().getNom());
        }


    }
  
      @Test
    public void testCommandes(){
        Librairie librairie = new Librairie(5,"Le Ch'ti livre","Lille");
        Commande commande = new Commande(0,"2020-01-01","N","M",365,5);
        
        assertTrue(commande.getLibrairie() == 5);
        assertTrue(commande.getNumCommande() == 0);
        assertTrue(commande.getDetail().equals(Arrays.asList(0,"2020-01-01","N","M",365,5)));
        assertFalse(commande.getEnLigne().equals("O"));
        assertFalse(commande.getId() == 360);
        assertTrue(commande.getId() == 365);
    
        commande.setNumCommande(1);
        
        assertEquals(1, commande.getNumCommande());
    }
}

