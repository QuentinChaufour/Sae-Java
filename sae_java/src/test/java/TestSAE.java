
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.mariadb.jdbc.client.Client;

public class TestSAE {
    

    @Test
    public void testClientBasics() {
        Client client = new Client("Julie", "Martin", 3, "133 boulevard de l''Université", "45000", "Orléans",new Librairie(7,"Loire et livres", "Orléans"));

        // Test getters
        assertEquals("Julie", client.getNom());
        assertEquals("Martin", client.getPrenom());
        assertEquals(3, client.getIdClient());
        assertEquals("133 boulevard de l''Université", client.getAdresse());
        assertEquals("45000", client.getCodePostal());
        assertEquals("Orléans", client.getVille());
        assertEquals(new Librairie(7,"Loire et livres", "Orléans"), client.getLibrairie());
        
        client.setLibrairie(new Librairie(1,"La librairie parisienne", "Paris"));
        client.setNom("Dupont");
        client.setPrenom("Jean");
        client.setAddresse("456 avenue de la République");
        client.setCodePostal("75000");
        client.setVille("Paris");

        assertEquals("Jean", client.getNom());
        assertEquals("Dupont", client.getPrenom());
        assertEquals("456 avenue de la République", client.getAdresse());
        assertEquals("75000", client.getCodePostal());
        assertEquals("Paris", client.getVille());
        assertEquals(new Librairie(1,"La librairie parisienne", "Paris"), client.getLibrairie());
        // pas de setteur pour idClient car c'est un attribut non modifiable (final)
    }

}
