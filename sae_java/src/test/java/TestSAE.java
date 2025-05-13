
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
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

        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur("1","H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");

        assertTrue(livre.getIsbn().equals("120"));
        assertEquals("La Guerre des mondes", livre.getTitre());
        assertTrue(livre.getAuteurs().contains(new Auteur("1","H.G. Wells",null,null)));
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
        Livre livre1 = new Livre("120","La Guerre des mondes",  Arrays.asList(new Auteur("1","H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");
        Livre livre2 = new Livre("121","Le Petit Prince",  Arrays.asList(new Auteur("2","Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman");

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

        Client client = new Client("Martin", "Julie", 3, "133 boulevard de l''Université", "45000", "Orléans",new Librairie(7,"Loire et livres", "Orléans"));

        // Test ajout/suppression de livres au panier du client
        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur("1","H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");
        
        try{
            client.ajouterAuPanier(livre,client.getLibrairie(), 1);
        }
        catch (QuantiteInvalideException e) {
            System.err.println("Quantité invalide");
        }
        
        assertTrue(client.getPanier().getLivres().contains(livre));
        assertFalse(client.getPanier().getLivres().contains(new Livre("121","Le Petit Prince", Arrays.asList(new Auteur("2","Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman")));

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
        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur("1","H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");
        Livre livre2 = new Livre("121","Le Petit Prince", Arrays.asList(new Auteur("2","Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman");

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
        Client client = new Client("Martin", "Julie", 3, "133 boulevard de l''Université", "45000", "Orléans",librairie);
        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur("1","H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");

        try{
            librairie.ajouterLivre(livre,3);
        }
        catch (QuantiteInvalideException e) {
            System.err.println("Quantité invalide");
        }

        assertTrue(client.consulterLivres().containsKey(livre));

    }
  
    @Test
    public void testCommandes(){
        Librairie librairie = new Librairie(5,"Le Ch'ti livre","Lille");
        Client client = new Client("Martin", "Julie", 3, "133 boulevard de l''Université", "45000", "Orléans",librairie);
        Commande commande = new Commande(0, new Date(), "O", "O", client, librairie);
        
        assertTrue(commande.getIdLibrairie() == 5);
        assertTrue(commande.getNumCommande() == 0);
        assertTrue(commande.getEnLigne().equals("O"));
        assertFalse(commande.getId() == 360);
        assertTrue(commande.getId() == 3);

        Livre livre = new Livre("120", "La Guerre des mondes", Arrays.asList(new Auteur("1", "H.G. Wells", null, null)),"Gallimard", 1898, 9.99, 159, "Science Fiction");
        try{
            DetailCommande detailCommande = new DetailCommande(1,livre,2);
            
            commande.addDetailCommande(detailCommande);

            assertTrue(commande.getDetails().contains(detailCommande));
            assertTrue(commande.getDetails().get(0).getLivre().equals(livre));
            assertTrue(commande.getDetails().get(0).getQuantite() == 2);
            assertFalse(commande.getDetails().get(0).getNumLig() == 10);
            assertTrue(commande.getDetails().get(0).getNumLig() == 1);
        } 
        catch(QuantiteInvalideException e){
            System.out.println("Quantité invalide");
        }

        try{
            commande.addDetailCommande(new DetailCommande(2, livre, 0));
        }
        catch(QuantiteInvalideException e){
            System.out.println("Quantité invalide");
        }
    }

    @Test
    public void testCommanderClient(){

        Livre livre = new Livre("120", "La Guerre des mondes", Arrays.asList(new Auteur("1", "H.G. Wells", null, null)),"Gallimard", 1898, 9.99, 159, "Science Fiction");
        Livre livre2 = new Livre("121","Le Petit Prince", Arrays.asList(new Auteur("2","Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman");

        // préparer la bd test
        try {
            Reseau.createStatement("insert into testCLIENT values (1,'Martin','Julie','133 boulevard de l universite','45000','Orleans')").executeUpdate();
            Reseau.createStatement("insert into testMAGASIN values (0,'Librairie de la Fac','Orleans')").executeUpdate();
            Reseau.createStatement("insert into testMAGASIN values (1,'La librairie du centre','Tours')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (2,'Dupont','Jean','456 avenue de la Republique','75000','Paris')").executeUpdate();

            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);

            Reseau.createStatement("insert into testAUTEUR values ('1','H.G Wells',null,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values ('2','Antoine de Saint-Exupéry',null,null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('120','La Guerre des mondes',159,1898,9.99,'Science Fiction','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('121','Le Petit Prince',96,1943,7.99,'Roman','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('120','1')").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('121','2')").executeUpdate();

            Reseau.createStatement("insert into testPOSSEDER values (0,'120',4)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'121',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'120',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'121',7)").executeUpdate();
             
        } catch (SQLException e) {
            System.err.println("pb insertion" + e.getMessage());
        }

        Client client = new Client("Martin", "Julie", 1, "133 boulevard de l''Université", "45000", "Orléans",Reseau.librairies.get(0));
        Client client2 = new Client("Dupont", "Jean", 2, "456 avenue de la République", "75000", "Paris",Reseau.librairies.get(1));

        try{
            client.ajouterAuPanier(livre,client.getLibrairie(), 2);
            client.ajouterAuPanier(livre2,client.getLibrairie(), 1);
            client.ajouterAuPanier(livre, Reseau.getLibrairie(1), 4);

            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);
            assertTrue(Reseau.checkStock(livre, Reseau.getLibrairie(client.getLibrairie().getId()), 4));
        }
        catch(QuantiteInvalideException e){
            System.out.println("Quantité invalide pour commande de livre");
        }
        catch (LibraryNotFoundException e){

        }

        
        assertTrue(client.commander());
        assertFalse(client2.commander());

        
        try{
            assertFalse(Reseau.checkStock(livre, Reseau.getLibrairie(client.getLibrairie().getId()), 4));
            Reseau.createStatement("delete from testDETAILCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testPOSSEDER").executeUpdate();
            Reseau.createStatement("delete from testECRIRE").executeUpdate();
            Reseau.createStatement("delete from testAUTEUR").executeUpdate();
            Reseau.createStatement("delete from testLIVRE").executeUpdate();
            Reseau.createStatement("delete from testMAGASIN").executeUpdate();
            Reseau.createStatement("delete from testCLIENT").executeUpdate();
            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);
        }
        catch (SQLException e){

        }
        catch (LibraryNotFoundException e){

        }
        
    }

    @Test
    public void testUpdateLibrairie(){

        Librairie librairie = new Librairie(0,"Librairie de la Fac","Orleans");
        Librairie librairie2 = new Librairie(1, "La librairie du centre", "Tours");

        Livre livre = new Livre("120", "La Guerre des mondes", Arrays.asList(new Auteur("1", "H.G. Wells", null, null)),"Gallimard", 1898, 9.99, 159, "Science Fiction");
        Livre livre2 = new Livre("121","Le Petit Prince", Arrays.asList(new Auteur("1","Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman");
        
        assertFalse(Reseau.librairies.contains(librairie));
        assertFalse(Reseau.librairies.contains(librairie2));

        try {
            Reseau.createStatement("insert into testMAGASIN values (0,'Librairie de la Fac','Orleans')").executeUpdate();

            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);

            assertTrue(Reseau.librairies.contains(librairie));
            assertFalse(Reseau.librairies.contains(librairie2));

            Reseau.createStatement("insert into testMAGASIN values (1,'La librairie du centre','Tours')").executeUpdate();
            
            Reseau.createStatement("insert into testAUTEUR values (1,'H.G Wells',null,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (2,'Antoine de Saint-Exupéry',null,null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('120','La Guerre des mondes',159,1898,9.99,'Science Fiction','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('121','Le Petit Prince',96,1943,7.99,'Roman','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('120',1)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('121',2)").executeUpdate();

            Reseau.createStatement("insert into testPOSSEDER values (0,'120',4)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'121',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'120',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'121',7)").executeUpdate();

        } catch (SQLException e) {
        }

        Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);

        try{
            assertTrue(Reseau.librairies.contains(librairie));
            assertTrue(Reseau.librairies.contains(librairie2));

            assertTrue(Reseau.getLibrairie(0).checkStock(livre, 3));
            assertFalse(Reseau.getLibrairie(1).checkStock(livre2, 10));
        }
        catch (LibraryNotFoundException e){

        }


        try {
            
            Reseau.createStatement("delete from testPOSSEDER").executeUpdate();
            Reseau.createStatement("delete from testECRIRE").executeUpdate();
            Reseau.createStatement("delete from testAUTEUR").executeUpdate();
            Reseau.createStatement("delete from testLIVRE").executeUpdate();
            Reseau.createStatement("delete from testMAGASIN").executeUpdate();
            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);

        } catch (SQLException e) {
        }

    }
}

