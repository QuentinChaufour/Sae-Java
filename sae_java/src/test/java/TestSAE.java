
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        assertEquals(new Panier(), client.getPanier());
        
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

        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur(1,"H.G. Wells")), "Gallimard", 1898,9.99, 159, "Science Fiction");

        assetEquals("120", livre.getIsbn());
        assertEquals("La Guerre des mondes", livre.getTitre());
        assertTrue(livre.getAuteurs().contains(new Auteur(1,"H.G. Wells")));
        assertEquals("Gallimard", livre.getEditeur());
        assertEquals(1898, livre.getAnneePublication());
        assertEquals(9.99, livre.getPrix(), 0.00);
        assertEquals(159, livre.getNombrePages());
        assertEquals("Science Fiction", livre.getClassification());

        livre.setPrix(12.99);
        
        assertEquals(12.99, livre.getPrix(), 0.00);
    }

    @Test
    public void testPanier() {

        Panier panier = new Panier();
        Livre livre1 = new Livre("120","La Guerre des mondes",  Arrays.asList(new Auteur(1,"H.G. Wells")), "Gallimard", 1898,9.99, 159, "Science Fiction");
        Livre livre2 = new Livre("121","Le Petit Prince",  Arrays.asList(new Auteur(2,"Antoine de Saint-Exupéry")), "Gallimard", 1943, 7.99, 96, "Roman");

        Librairie librairie = new Librairie(1, "La librairie parisienne", "Paris");
        // Test ajout de livres
        panier.ajouterLivre(livre1,librairie);
        panier.ajouterLivre(livre2,librairie);

        Map<Librairie,Map<Livre,Integer>> contenu = new HashMap<>();
        contenu.put(librairie, new HashMap<>());
        contenu.get(librairie).put(livre1, 1);
        contenu.get(librairie).put(livre2, 1);

        assertTrue(panier.getContenu().equals(contenu));
        assertTrue(panier.getLivres().contains(livre1));
        assertTrue(panier.getLivres().contains(livre2));

        assertEquals(17.98, panier.getPrixTotal(), 0.00);

        // Test suppression de livres
        panier.removeLivre(livre2,librairie);
        assertFalse(panier.getLivres().contains(livre2));

        panier.viderPanier();
        assertTrue(panier.getLivres().isEmpty());
        assertTrue(panier.getContenu().isEmpty());
        assertEquals(0.0, panier.getPrixTotal(), 0.00);

    }

    @Test
    public void testPanierClient(){

        Client client = new Client("Julie", "Martin", 3, "133 boulevard de l''Université", "45000", "Orléans",new Librairie(7,"Loire et livres", "Orléans"));

        // Test ajout/suppression de livres au panier du client
        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur(1,"H.G. Wells")), "Gallimard", 1898,9.99, 159, "Science Fiction");
        client.ajouterLivre(livre);
        assertTrue(client.getPanier().getContenu().contains(livre));
        assertFalse(client.getPanier().getContenu().contains(new Livre("121","Le Petit Prince", Arrays.asList(new Auteur(2,"Antoine de Saint-Exupéry")), "Gallimard", 1943, 7.99, 96, "Roman")));

        client.removeLivre(livre);
        assertFalse(client.getPanier().getContenu().contains(livre));
    }

}
