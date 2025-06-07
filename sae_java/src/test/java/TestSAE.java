
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TestSAE {
    

    @Test
    public void testClientBasics() {
        Client client = new Client("Julie", "Martin", "mot_de_passe_1439", 3, "133 boulevard de l''Université", "45000", "Orléans",7);

        // Test getters
        assertEquals("Julie", client.getNom());
        assertEquals("Martin", client.getPrenom());
        assertEquals(3, client.getId());
        assertEquals("133 boulevard de l''Université", client.getAddress());
        assertEquals("45000", client.getCodePostal());
        assertEquals("Orléans", client.getVille());
        assertEquals(7, client.getLibrairie());
        
        client.setLibrairie(1);
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
        assertEquals(1, client.getLibrairie());

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

        try {
            Reseau.addLibrairie(librairie);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Test ajout de livres
        panier.ajouterLivre(livre1,1,1);
        panier.ajouterLivre(livre2,1,1);

        Map<Integer,Map<Livre,Integer>> contenu = new HashMap<>();
        contenu.put(librairie.getId(), new HashMap<>());
        contenu.get(librairie.getId()).put(livre1, 1);
        contenu.get(librairie.getId()).put(livre2, 1);

        assertTrue(panier.getContenu().equals(contenu));

        assertEquals(17.98, panier.getPrixTotal(), 0.00);

        // Test suppression de livres
        try{
            panier.retirerLivre(livre2,1,1);
            assertFalse(panier.getContenu().get(1).containsKey(livre2));
        }catch (PasAssezDeStockException e) {
            System.err.println("Pas assez de stock pour retirer le livre");
        }
        panier.viderPanier();
        assertTrue(panier.getContenu().isEmpty());
        assertEquals(0.0, panier.getPrixTotal(), 0.00);

        try {
            Reseau.removeLibrairie(librairie);
        } catch (LibraryNotFoundException e) {
        }
        catch (SQLException e) {
        }

    }

    @Test
    public void testPanierClient(){

        Client client = new Client("Martin", "Julie", "mot_de_passe_1439", 3, "133 boulevard de l''Université", "45000", "Orléans",7);

        // Test ajout/suppression de livres au panier du client
        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur("1","H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");
        
        try{
            client.ajouterAuPanier(livre,client.getLibrairie(), 1);
        }
        catch (QuantiteInvalideException e) {
            System.err.println("Quantité invalide");
        }
        
        assertFalse(client.getPanier().getContenu().get(7).containsKey(new Livre("121","Le Petit Prince", Arrays.asList(new Auteur("2","Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman")));

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
            librairie.ajouterAuStock(livre, 5);
            assertTrue(librairie.consulterStock().containsKey(livre));
            assertTrue(librairie.consulterStock().get(livre) == 5);

            librairie.ajouterAuStock(livre, 2);
            assertTrue(librairie.consulterStock().get(livre) == 7);

            librairie.retirerLivre(livre,6);
            assertTrue(librairie.consulterStock().get(livre) == 1);

            librairie.retirerLivre(livre,1);
            assertFalse(librairie.consulterStock().containsKey(livre));

            librairie.ajouterAuStock(livre2, 0);
        }
        catch (QuantiteInvalideException e) {
            System.err.println("Quantité invalide");
        }
        catch (BookNotInStockException e) {
            System.err.println("Livre non disponible");
        }
        catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'ajout/suppression au stock");
        }


        try {
            librairie.ajouterAuStock(livre, 5);
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
        Client client = new Client("Martin", "Julie", "mot_de_passe_1439", 3, "133 boulevard de l''Université", "45000", "Orléans",librairie.getId());
        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur("1","H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");

        try {
            Reseau.addLibrairie(librairie);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            librairie.ajouterAuStock(livre,3);
        }
        catch (QuantiteInvalideException e) {
            System.err.println("Quantité invalide");
        }

        assertTrue(client.consulterLivres().containsKey(livre));
        try {
            Reseau.removeLibrairie(librairie);
        } catch (LibraryNotFoundException e) {
        }
        catch (SQLException e) {
        }

    }

    @Test
    public void testVendeur(){
        Librairie librairie = new Librairie(7,"Loire et livres", "Orléans");
        Vendeur vendeur = new Vendeur("Julie", "Martin", "1234", 3);
        Client client = new Client("Martin", "Julie", "mot_de_passe_1439", 3, "133 boulevard de l''Université", "45000", "Orléans",librairie.getId());
        Livre livre = new Livre("120","La Guerre des mondes", Arrays.asList(new Auteur("1","H.G. Wells",null,null)), "Gallimard", 1898,9.99, 159, "Science Fiction");
        Panier panier = new Panier();
        panier.ajouterLivre(livre, librairie.getId(), 1);
        Commande commande = new Commande(1, new java.util.Date(), "O", "O", client.getId(), librairie.getId());
        DetailCommande detailcommande;
        try {
            detailcommande = new DetailCommande(1, livre, 1);
            commande.addDetailCommande(detailcommande);

        } catch (QuantiteInvalideException e) {
            System.err.println("Quantité invalide");
        }
        List<Commande> listeCommandes = new ArrayList<>();
        listeCommandes.add(commande);
        Map<Livre, Integer> livresCommande = new HashMap<>();
        livresCommande.put(livre, 1);
        assertTrue(vendeur.checkQte(commande));
        assertEquals(listeCommandes, vendeur.preparerCommandes(client.getId(), livresCommande, new java.util.Date(), "O", "O"));
        assertEquals(livre, vendeur.transfererLivre(livre, librairie));
    }
 

    @Test
    public void testCommandes(){
        Librairie librairie = new Librairie(5,"Le Ch'ti livre","Lille");
        Client client = new Client("Martin", "Julie", "mot_de_passe_1439", 3, "133 boulevard de l''Université", "45000", "Orléans",librairie.getId());
        Commande commande = new Commande(0, new java.util.Date(), "O", "O", client.getId(), client.getLibrairie());
        try {
            Reseau.addLibrairie(librairie);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
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

        try {
            Reseau.removeLibrairie(librairie);
        } catch (LibraryNotFoundException e) {
        }
        catch (SQLException e) {
        }
    }

    @Test
    public void clearBD(){
        try {
            Reseau.createStatement("delete from testDETAILCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testPOSSEDER").executeUpdate();
            Reseau.createStatement("delete from testECRIRE").executeUpdate();
            Reseau.createStatement("delete from testAUTEUR").executeUpdate();
            Reseau.createStatement("delete from testLIVRE").executeUpdate();
            Reseau.createStatement("delete from testMAGASIN").executeUpdate();
            Reseau.createStatement("delete from testCLIENT").executeUpdate();
            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);
        } catch (SQLException e) {
            System.err.println("pb suppression clear DB" + e.getMessage());
        }
    }

    @Test
    public void testCommanderClient(){

        Livre livre = new Livre("120", "La Guerre des mondes", Arrays.asList(new Auteur("1", "H.G. Wells", null, null)),"Gallimard", 1898, 9.99, 159, "Science Fiction");
        Livre livre2 = new Livre("121","Le Petit Prince", Arrays.asList(new Auteur("2","Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman");

        // préparer la bd test
        try {

            Reseau.createStatement("insert into testMAGASIN values (0,'Librairie de la Fac','Orleans')").executeUpdate();
            Reseau.createStatement("insert into testMAGASIN values (1,'La librairie du centre','Tours')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (2,'Dupont','Jean','a','456 avenue de la Republique','75000','Paris')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (1,'Martin','Julie','b','133 boulevard de l universite','45000','Orleans')").executeUpdate();

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

        Client client = new Client("Martin", "Julie", "mot_de_passe_1439", 1, "133 boulevard de l''Université", "45000", "Orléans",0);
        Client client2 = new Client("Dupont", "Jean", "mot_de_passe_1439", 2, "456 avenue de la République", "75000", "Paris",1);

        try{
            client.ajouterAuPanier(livre,client.getLibrairie(), 2);
            client.ajouterAuPanier(livre2,client.getLibrairie(), 1);
            client.ajouterAuPanier(livre, 1, 4);

            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);
            assertTrue(Reseau.checkStock(livre, Reseau.getLibrairie(client.getLibrairie()), 4));

        }
        catch(QuantiteInvalideException e){
            System.out.println("Quantité invalide pour commande de livre");
        }
        catch (LibraryNotFoundException e){

        }

        assertTrue(client.commander("C",true,false));
        assertFalse(client2.commander("M",false,false));
        
        try{
            assertFalse(Reseau.checkStock(livre, Reseau.getLibrairie(client.getLibrairie()), 4));
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
            System.err.println("pb suppression commander client" + e.getMessage());
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

        Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);

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

    @Test
    public void testOnVousRecommande(){

        Client client = new Client("Martin", "Julie", "mot_de_passe_1439", 1, "133 boulevard de l''Université", "45000", "Orléans",0);

        try {
            Reseau.createStatement("insert into testMAGASIN values (0,'Librairie de la Fac','Orleans')").executeUpdate();
            Reseau.createStatement("insert into testMAGASIN values (1,'La librairie du centre','Tours')").executeUpdate();
            
            Reseau.createStatement("insert into testCLIENT values (3,'Eboue','Fabrice','a','60 avenue de la Republique','75000','Paris')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (2,'Dupont','Jean','b','456 avenue de la Republique','75000','Paris')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (1,'Martin','Julie','c','133 boulevard de l universite','45000','Orleans')").executeUpdate();

            Reseau.createStatement("insert into testAUTEUR values (1,'H.G Wells',null,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (2,'Antoine de Saint-Exupéry',null,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (3,'Jim Davis',1945,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (4,'Tui T. Sutherland',1978,null)").executeUpdate();
            
            Reseau.createStatement("insert into testLIVRE values ('120','La Guerre des mondes',159,1898,9.99,'Science Fiction','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('121','Le Petit Prince',96,1943,7.99,'Roman','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('122','Harry Potter',96,1943,7.99,'Roman','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('123','Hunger Games',96,1943,7.99,'Science Fiction','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('124','Garfiel & Cie',96,1943,7.99,'BD','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('125','La Guerre des Clans',96,1943,7.99,'Roman','PKJ',null)").executeUpdate();
            
            Reseau.createStatement("insert into testECRIRE values ('120',1)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('121',2)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('124',3)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('125',4)").executeUpdate();

            Reseau.createStatement("insert into testPOSSEDER values (0,'120',4)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'121',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'122',2)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'124',1)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'125',9)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'120',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'121',7)").executeUpdate();
            

            PreparedStatement statement = Reseau.createStatement("insert into testCOMMANDE values (1,?,'O','M',1,1)");
            
            Date sqlDate = new Date(new java.util.Date().getTime());

            statement.setDate(1, sqlDate);
            statement.executeUpdate();
            PreparedStatement statement2 = Reseau.createStatement("insert into testCOMMANDE values (2,?,'O','M',2,1)");

            statement2.setDate(1, sqlDate);
            statement2.executeUpdate();
            PreparedStatement statement3 = Reseau.createStatement("insert into testCOMMANDE values (3,?,'O','M',3,1)");

            statement3.setDate(1, sqlDate);
            statement3.executeUpdate();

            Reseau.createStatement("insert into testDETAILCOMMANDE values (1,1,1,9.99,'120')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (1,2,2,20,'121')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (2,3,1,9.99,'120')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (2,4,1,9.99,'121')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (2,5,1,9.99,'124')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (3,6,1,9.99,'120')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (3,7,1,9.99,'122')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (3,8,1,9.99,'123')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (3,9,1,9.99,'125')").executeUpdate();

            //System.out.println(client.OnVousRecommande());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Set<Livre> userBooks = new HashSet<>();
        userBooks.add(new Livre("120", "La Guerre des mondes", Arrays.asList(new Auteur("1", "H.G. Wells", null, null)),"Gallimard", 1898, 9.99, 159, "Science Fiction"));
        userBooks.add(new Livre("121","Le Petit Prince", Arrays.asList(new Auteur("1","Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman"));

        assertTrue(userBooks.equals(Reseau.getUserBooks(client.getId())));

        userBooks = null;
            
        Livre potter = new Livre("122", "Harry Potter", new ArrayList<>(),"Gallimard", 1943, 7.99, 96, "Romand");
        Livre clanWar = new Livre("125", "La Guerre des Clans", Arrays.asList(new Auteur("4","Tui T. Sutherland",1978,null)),"PKJ", 1943, 7.99, 96, "Romand");

        try {
            assertEquals(Arrays.asList(potter,clanWar),client.OnVousRecommande());
        } catch (LibraryNotFoundException ex) {
            System.err.println("Library not found");
        }


        try {
            Reseau.createStatement("delete from testDETAILCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testPOSSEDER").executeUpdate();
            Reseau.createStatement("delete from testECRIRE").executeUpdate();
            Reseau.createStatement("delete from testAUTEUR").executeUpdate();
            Reseau.createStatement("delete from testLIVRE").executeUpdate();
            Reseau.createStatement("delete from testMAGASIN").executeUpdate();
            Reseau.createStatement("delete from testCLIENT").executeUpdate();

        } catch (SQLException e) {
        }
    }

    @Test
    public void testIdentification(){

        Client client = new Client("Martin", "Julie", "mot_de_passe_1439", 1, "133 boulevard de l'Université", "45000", "Orléans",0);
        Client client2 = new Client("Dupont", "Jean", "mot_de_passe_1439", 2, "456 avenue de la République", "75000", "Paris",1);
        Client client3 = new Client("Eboue", "Fabrice", "mot_de_passe_1439", 3, "60 avenue de la République", "75000", "Paris",1);

        try {
            
            Reseau.createStatement("insert into testCLIENT values (3,'Eboue','Fabrice','Tient!!!','60 avenue de la Republique','75000','Paris')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (2,'Dupont','Jean','riviere','456 avenue de la Republique','75000','Paris')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (1,'Martin','Julie','pecheur','133 boulevard de l''Université','45000','Orléans')").executeUpdate();

            Client identifie = Reseau.identificationClient("Martin", "Julie","pecheur" , 0);
            assertEquals(client, identifie);

            Client identifie2 = Reseau.identificationClient("Dupont", "Jean","riviere" , 0);
            assertEquals(client2, identifie2);

            Client identifie3 = Reseau.identificationClient("Eboue", "Fabrice","Tient!!!" , 0);
            assertEquals(client3, identifie3);


            Client identifie4 = Reseau.identificationClient("Niel", "Xavier","maitreDeLaCom" , 0);
            // émet une erreur car le client n'existe pas

        } catch (SQLException e) {
            System.err.println("pb insertion identificationClient" + e.getMessage());
        }
        catch (NoCorrespondingClient e) {
            System.out.println(e.getMessage());
        }

        try {
            Reseau.createStatement("delete from testCLIENT").executeUpdate();
        } catch (SQLException e) {
        }
    }


    @Test
    public void testAjoutLivreVendeurAdmin(){

        Livre livre = new Livre("120", "La Guerre des mondes", Arrays.asList(new Auteur("1", "H.G. Wells", null, null)),"Gallimard", 1898, 9.99, 159, "Science Fiction");
        Livre livre2 = new Livre("121","Le Petit Prince", Arrays.asList(new Auteur("2","Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman");
        Livre potter = new Livre("122", "Harry Potter", new ArrayList<>(),"Gallimard", 1943, 7.99, 96, "Romand");
        Livre clanWar = new Livre("125", "La Guerre des Clans", Arrays.asList(new Auteur("4","Tui T. Sutherland",1978,null)),"PKJ", 1943, 7.99, 96, "Romand");

        try
        {
            Reseau.createStatement("insert into testMAGASIN values (0,'Librairie de la Fac','Orleans')").executeUpdate();

            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);

            Librairie librairie = Reseau.getLibrairie(0);

            librairie.ajouterNouveauLivre(livre, 2);
            librairie.ajouterNouveauLivre(potter, 3);

            assertTrue(librairie.consulterStock().containsKey(livre));
            assertTrue(librairie.consulterStock().get(livre) == 2);
            assertTrue(librairie.consulterStock().containsKey(potter));
            assertTrue(librairie.consulterStock().get(potter) == 3);
            assertFalse(librairie.consulterStock().containsKey(livre2));

            Reseau.updateInfos(EnumUpdatesDB.STOCKS);
            // si les livres sont bien ajoutés, ils seront toujours dans le stock de la librairie

            librairie = Reseau.getLibrairie(0);

            assertTrue(librairie.consulterStock().containsKey(livre));
            assertTrue(librairie.consulterStock().get(livre) == 2);
            assertTrue(librairie.consulterStock().containsKey(potter));
            assertTrue(librairie.consulterStock().get(potter) == 3);
            assertFalse(librairie.consulterStock().containsKey(clanWar));


        } catch (SQLException e) {
            System.err.println("pb insertion test ajout livre Vendeur/Admin" + e.getMessage());
        }
        catch (QuantiteInvalideException e) {
            System.err.println("Quantité invalide pour l'ajout du livre");
        }
        catch (LibraryNotFoundException e) {
            System.err.println("Library not found");
        }



        try {
            Reseau.createStatement("delete from testDETAILCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testCOMMANDE").executeUpdate();
            Reseau.createStatement("delete from testPOSSEDER").executeUpdate();
            Reseau.createStatement("delete from testECRIRE").executeUpdate();
            Reseau.createStatement("delete from testAUTEUR").executeUpdate();
            Reseau.createStatement("delete from testLIVRE").executeUpdate();
            Reseau.createStatement("delete from testMAGASIN").executeUpdate();
            Reseau.createStatement("delete from testCLIENT").executeUpdate();
        } catch (SQLException e) {
        }
    }

    @Test
    public void testLogAdmin(){

        assertFalse(Reseau.identificationAdmin("sae", "1234"));
        assertFalse(Reseau.identificationAdmin("revan", "1234"));
        assertTrue(Reseau.identificationAdmin("revan", "Two_Face"));

    }

    @Test
    public void testfacture(){
        
        Client client = new Client("Martin", "Julie", "mot_de_passe_1439", 1, "133 boulevard de l''Université", "45000", "Orléans",0);

        Livre livre = new Livre("120", "La Guerre des mondes", Arrays.asList(new Auteur("1", "H.G. Wells", null, null)),"Gallimard", 1898, 9.99, 159, "Science Fiction");
        Livre livre2 = new Livre("121","Le Petit Prince", Arrays.asList(new Auteur("2","Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman");
        Livre potter = new Livre("122", "Harry Potter", new ArrayList<>(),"Gallimard", 1943, 7.99, 96, "Romand");
        Livre clanWar = new Livre("125", "La Guerre des Clans", Arrays.asList(new Auteur("4","Tui T. Sutherland",1978,null)),"PKJ", 1943, 7.99, 96, "Romand");

        // préparer la bd test
        try {

            Reseau.createStatement("insert into testMAGASIN values (0,'Librairie de la Fac','Orleans')").executeUpdate();
            Reseau.createStatement("insert into testMAGASIN values (1,'La librairie du centre','Tours')").executeUpdate();
            
            Reseau.createStatement("insert into testCLIENT values (3,'Eboue','Fabrice','a','60 avenue de la Republique','75000','Paris')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (2,'Dupont','Jean','b','456 avenue de la Republique','75000','Paris')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (1,'Martin','Julie','c','133 boulevard de l universite','45000','Orleans')").executeUpdate();

            Reseau.createStatement("insert into testAUTEUR values (1,'H.G Wells',null,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (2,'Antoine de Saint-Exupéry',null,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (3,'Jim Davis',1945,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (4,'Tui T. Sutherland',1978,null)").executeUpdate();
            
            Reseau.createStatement("insert into testLIVRE values ('120','La Guerre des mondes',159,1898,9.99,'Science Fiction','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('121','Le Petit Prince',96,1943,7.99,'Roman','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('122','Harry Potter',96,1943,7.99,'Roman','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('123','Hunger Games',96,1943,7.99,'Science Fiction','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('124','Garfiel & Cie',96,1943,7.99,'BD','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('125','La Guerre des Clans',96,1943,7.99,'Roman','PKJ',null)").executeUpdate();
            
            Reseau.createStatement("insert into testECRIRE values ('120',1)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('121',2)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('124',3)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('125',4)").executeUpdate();

            Reseau.createStatement("insert into testPOSSEDER values (0,'120',4)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'121',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'122',2)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'124',1)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'125',9)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'120',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'121',7)").executeUpdate();

            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);
            Reseau.updateInfos(EnumUpdatesDB.NUMCOM);
             
        } catch (SQLException e) {
            System.err.println("pb insertion test facture" + e.getMessage());
        }

        try{
            client.ajouterAuPanier(livre,client.getLibrairie(), 2);
            client.ajouterAuPanier(livre2,client.getLibrairie(), 1);
            client.ajouterAuPanier(potter, 1, 1);
            client.ajouterAuPanier(clanWar, 1, 3);


            assertTrue(Reseau.checkStock(livre, Reseau.getLibrairie(client.getLibrairie()), 4));

        }
        catch(QuantiteInvalideException e){
            System.out.println("Quantité invalide pour commande de livre");
        }
        catch (LibraryNotFoundException e){

        }

        client.commander("C",true,true);
        
        try{
            assertFalse(Reseau.checkStock(livre, Reseau.getLibrairie(client.getLibrairie()), 4));
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
            System.err.println("pb suppression commander client" + e.getMessage());
        }
        catch (LibraryNotFoundException e){
        }
    }

    @Test
    public void testStats(){

        Client client = new Client("Martin", "Julie", "mot_de_passe_1439", 1, "133 boulevard de l''Université", "45000", "Orléans",0);

        Livre livre = new Livre("120", "La Guerre des mondes", Arrays.asList(new Auteur("1", "H.G. Wells", null, null)),"Gallimard", 1898, 9.99, 159, "Science Fiction");
        Livre livre2 = new Livre("121","Le Petit Prince", Arrays.asList(new Auteur("2","Antoine de Saint-Exupéry",null,null)), "Gallimard", 1943, 7.99, 96, "Roman");
        Livre potter = new Livre("122", "Harry Potter", new ArrayList<>(),"Gallimard", 1943, 7.99, 96, "Romand");
        Livre clanWar = new Livre("125", "La Guerre des Clans", Arrays.asList(new Auteur("4","Tui T. Sutherland",1978,null)),"PKJ", 1943, 7.99, 96, "Romand");

        // préparer la bd test
        try {

            Reseau.createStatement("insert into testMAGASIN values (0,'Librairie de la Fac','Orleans')").executeUpdate();
            Reseau.createStatement("insert into testMAGASIN values (1,'La librairie du centre','Tours')").executeUpdate();
            
            Reseau.createStatement("insert into testCLIENT values (3,'Eboue','Fabrice','a','60 avenue de la Republique','75000','Paris')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (2,'Dupont','Jean','b','456 avenue de la Republique','75000','Paris')").executeUpdate();
            Reseau.createStatement("insert into testCLIENT values (1,'Martin','Julie','c','133 boulevard de l universite','45000','Orleans')").executeUpdate();

            Reseau.createStatement("insert into testAUTEUR values (1,'H.G Wells',null,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (2,'Antoine de Saint-Exupéry',null,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (3,'Jim Davis',1945,null)").executeUpdate();
            Reseau.createStatement("insert into testAUTEUR values (4,'Tui T. Sutherland',1978,null)").executeUpdate();
            
            Reseau.createStatement("insert into testLIVRE values ('120','La Guerre des mondes',159,1898,9.99,'Science Fiction','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('121','Le Petit Prince',96,1943,7.99,'Roman','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('122','Harry Potter',96,1943,7.99,'Roman','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('123','Hunger Games',96,1943,7.99,'Science Fiction','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('124','Garfiel & Cie',96,1943,7.99,'BD','Gallimard',null)").executeUpdate();
            Reseau.createStatement("insert into testLIVRE values ('125','La Guerre des Clans',96,1943,7.99,'Roman','PKJ',null)").executeUpdate();
            
            Reseau.createStatement("insert into testECRIRE values ('120',1)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('121',2)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('124',3)").executeUpdate();
            Reseau.createStatement("insert into testECRIRE values ('125',4)").executeUpdate();

            Reseau.createStatement("insert into testPOSSEDER values (0,'120',4)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'121',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'122',2)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (0,'124',1)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'125',9)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'120',5)").executeUpdate();
            Reseau.createStatement("insert into testPOSSEDER values (1,'121',7)").executeUpdate();


            PreparedStatement statement = Reseau.createStatement("insert into testCOMMANDE values (1,?,'O','M',1,1)");
            
            Date sqlDate = new Date(new java.util.Date().getTime());

            statement.setDate(1, sqlDate);
            statement.executeUpdate();
            PreparedStatement statement2 = Reseau.createStatement("insert into testCOMMANDE values (2,?,'O','M',2,1)");

            statement2.setDate(1, sqlDate);
            statement2.executeUpdate();
            PreparedStatement statement3 = Reseau.createStatement("insert into testCOMMANDE values (3,?,'O','M',3,1)");

            statement3.setDate(1, sqlDate);
            statement3.executeUpdate();

            PreparedStatement statement4 = Reseau.createStatement("insert into testCOMMANDE values (4,?,'O','M',3,0)");

            statement4.setDate(1, sqlDate);
            statement4.executeUpdate();

            Reseau.createStatement("insert into testDETAILCOMMANDE values (1,1,1,9.99,'120')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (1,2,2,20,'121')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (2,3,1,9.99,'120')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (2,4,1,9.99,'124')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (3,5,1,9.99,'120')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (3,6,1,9.99,'122')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (3,7,1,9.99,'123')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (3,8,1,9.99,'125')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (4,9,1,9.99,'120')").executeUpdate();
            Reseau.createStatement("insert into testDETAILCOMMANDE values (4,10,1,9.99,'125')").executeUpdate();



            Reseau.updateInfos(EnumUpdatesDB.LIBRAIRIE);
            Reseau.updateInfos(EnumUpdatesDB.NUMCOM);
             
        } catch (SQLException e) {
            System.err.println("pb insertion test stats" + e.getMessage());
        }

        try{

                assertTrue(Reseau.getPalmares(10, EnumPalmares.LIBRAIRIE).get(Reseau.getLibrairie(1)) == 9);

                int nbVenteLivre = Reseau.getPalmares(10, EnumPalmares.AUTEUR).get(new Auteur("2","Antoine de Saint-Exupéry",null,null));
                assertTrue(nbVenteLivre == 2);
                nbVenteLivre = Reseau.getPalmares(10, EnumPalmares.AUTEUR).get(new Auteur("1","H.G Wells",null,null));
                assertTrue(nbVenteLivre == 4);

                int venteLivre = Reseau.getPalmares(10, EnumPalmares.LIVRE).get(clanWar);
                assertTrue(venteLivre == 2);
                venteLivre = Reseau.getPalmares(10, EnumPalmares.LIVRE).get(livre);
                assertTrue(venteLivre == 4);    

                
                double chiffreAffaire = Reseau.CAByLibrairie().get(Reseau.getLibrairie(1));
                assertEquals(109.93, chiffreAffaire,0.01);

        }
        catch (SQLException e){
            System.err.println("pb stats " + e.getMessage());
        }
        catch (LibraryNotFoundException e) {
            e.printStackTrace();
        }

        try{
            
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
            System.err.println("pb suppression stats " + e.getMessage());
        }


    }
}

