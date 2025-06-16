package com.sae_java.Vue.alert;

import com.sae_java.Modele.Auteur;
import com.sae_java.Modele.Livre;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookInfoAlert {

    public BookInfoAlert(Livre livre,ImageView bookImage) {
       
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);

        BorderPane root = new BorderPane();
        ImageView imageView = new ImageView(bookImage.getImage());
        imageView.setFitWidth(50);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        root.setLeft(imageView); // obligé car une image a un seul endroit
        BorderPane.setMargin(imageView, new Insets(40)); // Set margin for the image

        root.setStyle("-fx-padding: 10; -fx-font-size: 14px; -fx-font-family: 'Arial';"); // Set padding and font style

        // Create a VBox to hold the book information

        VBox bookInfo = new VBox();

        Label titleLabel = new Label("ISBN: " + livre.getIsbn() + " " + livre.getTitre());

        Label auteursLabel = new Label("Auteurs :");

        bookInfo.getChildren().addAll(titleLabel,auteursLabel);

        if(livre.getAuteurs().isEmpty()) {
            Label noAuthorsLabel = new Label("Aucun auteur connu");
            bookInfo.getChildren().add(noAuthorsLabel);
        }

        for(Auteur auteur : livre.getAuteurs()) {

            String dteMort;
            String dteNaissance;

            if(auteur.getDteMort() == null) {
                dteMort = "Aujourd'hui";
            }
            else {
                dteMort = auteur.getDteMort()+"";
            }

            if(auteur.getDteNaissance() == null) {
                dteNaissance = "Inconnu";
            }
            else {
                dteNaissance = auteur.getDteNaissance()+"";
            }

            Label authorLabel = new Label("    - " + auteur.getNomPrenom() + "( " + dteNaissance +  " - " + dteMort + ")");
            bookInfo.getChildren().add(authorLabel);
        }


        Label publisherLabel = new Label("Editeur : " + livre.getEditeur());
        Label yearLabel = new Label("Annee de publication : " + livre.getDatePublication());
        Label priceLabel = new Label("Prix unitaire : " + livre.getPrix() + " €");

        Label pagesLabel = new Label("Nombre de pages : " + livre.getNbPages());
        bookInfo.getChildren().addAll(publisherLabel, yearLabel, pagesLabel, priceLabel);

        root.setCenter(bookInfo);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.setTitle("Book Information");
        stage.showAndWait();
    }
}