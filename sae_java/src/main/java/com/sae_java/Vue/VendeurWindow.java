package com.sae_java.Vue;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import com.sae_java.Vue.controleur.*;

import com.sae_java.Modele.Vendeur;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;

public class VendeurWindow extends BorderPane{

    private final ApplicationSAE app;
    private final Vendeur vendeur;


    public VendeurWindow(ApplicationSAE app, Vendeur vendeur) {
        this.app = app;
        this.vendeur = vendeur;
        
        this.minHeightProperty().set(600);
        this.minWidthProperty().set(1200);

        // top of the borderPane

        BorderPane top = new BorderPane();
        

        // Create a button for "Deconnexion"
        Button deconnexion = new Button("Deconnexion");
        deconnexion.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/deconnexion_32px.png"))));
        deconnexion.setOnAction(new ControleurDeconnexion(this.app));

        // create a button for Panier 


        BorderPane.setMargin(top, new Insets(0, 0, 10, 0));
        BorderPane.setMargin(deconnexion, new Insets(5));
        BorderPane.setAlignment(deconnexion, Pos.CENTER_LEFT);


        top.setLeft(deconnexion);

        this.setTop(top);

        // left side of the borderPane

        // client infos

        HBox vendeurInfo = new HBox(10);
        vendeurInfo.setStyle("-fx-padding: 10; -fx-background-color: #d7fffb; -fx-border-color: #000000; -fx-border-width: 2;");
        Label vendeurName = new Label(app.getVendeur().getNom());
        Label vendeurForname = new Label(app.getVendeur().getPrenom());
        ImageView vendeurImage = new ImageView(new Image(getClass().getResourceAsStream("/images/utilisateur.png")));

        vendeurImage.setFitHeight(32);
        vendeurImage.setFitWidth(32);
        vendeurImage.setPreserveRatio(true);
        vendeurImage.setSmooth(true);

        BorderPane.setAlignment(vendeurImage, Pos.CENTER_LEFT);
        vendeurName.setAlignment(Pos.CENTER);
        vendeurForname.setAlignment(Pos.CENTER);

        vendeurInfo.getChildren().addAll(vendeurImage, vendeurName, vendeurForname);

        VBox left = new VBox(10);

        // buttons

        Label librairieLabel = new Label();
        try {
            librairieLabel.setText("Librairie : " + Reseau.getLibrairie(app.getVendeur().getLibrairie()));
        } 
        catch (LibraryNotFoundException e) {

            // alert ?

            e.printStackTrace();
        }
        // add image to the button for ergonomics

        Button changerInfos = new Button("Update infos");
        changerInfos.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/reglages_32px.png"))));


        left.getChildren().addAll(vendeurInfo,librairieLabel, changerInfos);
        left.setStyle("-fx-margin: 10;-fx-padding: 10; -fx-background-color: #ffcae2;");
        this.setLeft(left);

        // center of the borderPane
        VBox center = new VBox(10);
        VBox addLivre = new VBox(5);
        addLivre.setPadding(new Insets(15));

        VBox insideAddLivre = new VBox(3);
        insideAddLivre.setPadding(new Insets(10));
        insideAddLivre.setStyle("-fx-background-color: #d7fffb;");

        Label addLivreLabel = new Label("Ajouter un livre");

        HBox addLivreInputBox = new HBox(10);

        Label isbnLabel = new Label("ISBN :");
        TextField isbnArea = new TextField();

        Label quantityLabel = new Label("Quantité :");
        TextField quantityArea = new TextField();

        Button addButton = new Button("Ajouter");

        addLivreInputBox.getChildren().addAll(isbnLabel, isbnArea, quantityLabel, quantityArea, addButton);
        insideAddLivre.getChildren().addAll(addLivreLabel, addLivreInputBox);
        addLivre.getChildren().add(insideAddLivre);



        VBox updateQuantity = new VBox(5);
        VBox insideUpdateQuantity = new VBox(3);
        Label addUpdateQuantityLabel = new Label("Modifier quantité");
        addLivre.getChildren().addAll(insideUpdateQuantity);
        insideUpdateQuantity.getChildren().addAll(addUpdateQuantityLabel);


        VBox checkAvailability = new VBox(5);

        center.getChildren().addAll(addLivre, updateQuantity, checkAvailability);


        // bottom of the borderPane
        HBox bottom = new HBox(10);
        Label credit = new Label("V1 par Jolan");
        bottom.getChildren().add(credit);

        this.setCenter(center);
        this.setBottom(bottom);

    }

    public Vendeur getVendeur() {
        return this.vendeur;
    }

}
