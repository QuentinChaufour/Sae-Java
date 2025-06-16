package com.sae_java.Vue;

import java.util.Map;

import com.sae_java.Vue.alert.BookInfoAlert;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.sae_java.Modele.Client;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Librairie;

public class PanierClientWindow {
    

    private final ApplicationSAE app;
    private final Client client;

    public PanierClientWindow(ApplicationSAE app, Client client) {
        this.app = app;
        this.client = client;

        // Initialize the UI components for the PanierClientWindow
        Stage stage = new Stage();
         
        stage.setTitle("Panier");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/panier_32px.png")));
        initUI(stage);
    }

    private void initUI(Stage stage) {

        try {
            BorderPane root = new BorderPane();
            root.setTop(new Label("Panier de " + client.getNom()));

            // center

            VBox center = new VBox(10);
            center.setAlignment(Pos.CENTER);

            for (Integer libID : client.getPanier().getContenu().keySet()) {

                Librairie lib = Reseau.getLibrairie(libID);

                Map<Livre, Integer> livres = client.getPanier().getContenu().get(lib);

                VBox books = new VBox(5);

                for (Livre book : livres.keySet()) {
                    int quantite = livres.get(book);

                    HBox bookBox = new HBox(10);
                    HBox infosBookBox = new HBox(10);
                    infosBookBox.setPrefWidth(750);
                    infosBookBox.setAlignment(Pos.CENTER_LEFT);
                    bookBox.setStyle("-fx-margin:10 ;-fx-padding: 15;");
                    infosBookBox.setStyle("-fx-margin:10 ;-fx-padding: 10; -fx-background-color: #d7fffb;");

                    // to change when we have images of the books

                    ImageView bookImage = new ImageView(
                            new Image(getClass().getResourceAsStream("/images/insertion_image.png")));
                    bookImage.setFitHeight(64);
                    bookImage.setFitWidth(64);
                    bookImage.setPreserveRatio(true);
                    bookImage.setSmooth(true);

                    String bookName = book.getTitre();

                    if (bookName.length() > 30) {
                        bookName = bookName.substring(0, 30);
                        bookName += "...";
                    }

                    Label bookTitle = new Label("Titre : " + bookName);
                    bookTitle.setPrefWidth(400);
                    Label bookPrice = new Label("Prix : " + book.getPrix() + "€");
                    bookPrice.setPrefWidth(75);
                    bookPrice.setAlignment(Pos.CENTER_RIGHT);
                    Label bookStock = new Label("Quantité commandé : " + quantite);
                    bookStock.setPrefWidth(75);
                    bookStock.setAlignment(Pos.CENTER_RIGHT);

                    bookTitle.setStyle("-fx-font-size: 14px;");
                    bookPrice.setStyle("-fx-font-size: 14px;");
                    bookStock.setStyle("-fx-font-size: 14px;");

                    Button updateQte = new Button("modifier quantité");
                    // addToPanier.setGraphic(new ImageView(new
                    // Image(getClass().getResourceAsStream("/images/ajout_16px.png"))));
                    updateQte.setAlignment(Pos.CENTER_RIGHT);

                    TextField quantityField = new TextField();
                    quantityField.setPromptText("Quantité");
                    quantityField.setPrefWidth(100);
                    // quantityField.setAlignment(Pos.CENTER_LEFT);
                    HBox.setMargin(quantityField, new Insets(5));
                    HBox.setMargin(updateQte, new Insets(5));
                    // pop up si nb négatif ou not a number quand ajouter au panier

                    Button infoBtn = new Button("More Info");
                    infoBtn.setOnAction(e -> new BookInfoAlert(book, bookImage));

                    infosBookBox.getChildren().addAll(bookImage, bookTitle, infoBtn, bookPrice, bookStock);
                    infosBookBox.setAlignment(Pos.CENTER_LEFT);

                    bookBox.getChildren().addAll(infosBookBox, quantityField, updateQte);
                    bookBox.setAlignment(Pos.CENTER);

                    books.getChildren().add(bookBox);
                }

                TitledPane libPane = new TitledPane(lib.getNom(), books);
                libPane.setCollapsible(false);
                libPane.setPadding(new Insets(10));

                center.getChildren().add(libPane);

                root.setCenter(center);

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.sizeToScene();

                stage.show();
            }
        }
        catch (LibraryNotFoundException e){

        }
    }

}
