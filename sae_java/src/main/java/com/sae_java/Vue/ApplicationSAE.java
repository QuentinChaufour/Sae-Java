package com.sae_java.Vue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sae_java.Modele.Client;
import com.sae_java.Modele.Vendeur;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class ApplicationSAE extends Application {

    private Stage stage;
    private Scene scene;

    private Client client;
    private Vendeur vendeur;

    public static int height;
    public static int width;

    private Connection connection;

    @Override
    public void init(){
    /*
        // récupérer le plus grand numCom existant
        try {
            Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/testImage", "root", "root_password");
            this.connection = connection;
            // Récupère le plus grand numCom existant
            
        }catch (SQLException e) {
            e.printStackTrace();
           }

        this.vendeur = new Vendeur("Dupont", "Jean", "1234",1, 1);

        //this.imageView = new ImageView();
        //this.imageView.setFitWidth(200);
        //this.imageView.setFitHeight(200);
        */

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // Get width and height
        ApplicationSAE.width = (int)bounds.getWidth();
        ApplicationSAE.height = (int)bounds.getHeight() - 30;
    }
    
    @Override
    public void start(Stage stage){

        BorderPane root = new FenetreConnexion(this).getRoot();
        /*
        InputStream inputStream = getClass().getResourceAsStream("/images/quitter.png");
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        */
        
        //BorderPane root = new BorderPane();

        //Button filechooser = new Button("Choisir une image");
        //filechooser.setOnAction(new ControleurFileChooser(this, this.stage));
        
        //root.setTop(filechooser);

        this.scene = new Scene(root);
        this.stage = stage;

        this.stage.setScene(this.scene);
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.setTitle("Mon application analyse");
        this.stage.centerOnScreen();
        this.stage.getIcons().add(new Image(getClass().getResource("/images/icon.png").toString()));
        this.stage.show();

    }

    public Stage getStage() {
        return stage;
    }
    
    public Scene getScene(){
        return this.scene;
    }

    public void quitter(){
        Platform.exit();
    }

    public static void main(String[] args){
        launch(args);
    }

    public void inputDB(File monImage) {
        try {

            //File monImage = new File(getClass().getResource("/images/quitter.png").toString());
            
            FileInputStream fileInputStream = new FileInputStream(monImage);

            PreparedStatement statement = this.connection.prepareStatement("INSERT INTO IMAGE values (?, ?)");

            statement.setInt(1, 1);
            statement.setBinaryStream(2, fileInputStream, (int) monImage.length());

            statement.executeUpdate();
            statement.close();
            fileInputStream.close();

        } catch (SQLException e) {
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayImage(int id){
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT img FROM IMAGE WHERE id = 1");

            ResultSet resultSet = statement.executeQuery();

            ImageView imageView = null;

            if(resultSet.next()) {
                byte[] imageBytes = resultSet.getBytes("img");
                Image image = new Image(new ByteArrayInputStream(imageBytes));
                imageView = new ImageView(image);
            }

            resultSet.close();
            statement.close();
            BorderPane root =  (BorderPane) this.stage.getScene().getRoot();
            root.setCenter(imageView);

            stage.sizeToScene();

        } catch (SQLException e) {
        }
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client){
        this.client = client;
    }

     public Vendeur getVendeur() {
        return this.vendeur;
    }

    public void loadIdentification(){

        BorderPane root = new FenetreConnexion(this).getRoot();

        this.scene = new Scene(root);

        this.stage.setScene(this.scene);
        this.stage.setResizable(false);
        this.stage.sizeToScene();
        this.stage.setTitle("Identification");
        this.stage.centerOnScreen();
        this.stage.getIcons().add(new Image(getClass().getResource("/images/icon.png").toString()));
        this.stage.show();        
    }
}