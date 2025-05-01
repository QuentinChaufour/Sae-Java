
import static org.junit.Assert.assertEquals;
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

}
