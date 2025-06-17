package com.sae_java.Vue;

import java.util.Map;

import com.sae_java.Vue.alert.BookInfoAlert;

import javafx.event.ActionEvent;
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
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import com.sae_java.Modele.Client;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Librairie;

public class PanierClientWindow extends BorderPane{
    

    private final ApplicationSAE app;
    private final Client client;

    private Button home;
    private Button commander;
    private Button previousPage;
    private Button nextPage;

    public PanierClientWindow(ApplicationSAE app) {
        super();
        this.app = app;
        this.client = this.app.getClient();

        // init btn
        this.commander = new Button("Commander");
        this.commander.setFont(Font.font("arial", 22));
        this.home = new  Button("Home");
        this.home.setFont(Font.font("arial", 22));
        this.home.setOnAction((ActionEvent) -> {this.app.getStage().setScene(new Scene(new ClientWindow(app)));});

        this.previousPage = new Button();
        this.previousPage.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/left_arrow_32px.png"))));

        this.nextPage = new Button();
        this.nextPage.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/right_arrow_32px.png"))));

        // size

        this.commander.setPrefWidth(0.2 * ApplicationSAE.width);
        this.home.setPrefWidth(0.2 * ApplicationSAE.width);
        this.previousPage.setPrefWidth(0.05 * ApplicationSAE.width);
        this.nextPage.setPrefWidth(0.05 * ApplicationSAE.width);

        this.commander.setPrefHeight(0.035 * ApplicationSAE.height);
        this.home.setPrefHeight(0.035 * ApplicationSAE.height);
        this.previousPage.setPrefHeight(0.035 * ApplicationSAE.height);
        this.nextPage.setPrefHeight(0.035 * ApplicationSAE.height);

        if(this.client.getPanier().getContenu().isEmpty()){
            this.initEmptyUI();
        }
        else{
            this.initUI();
        }
        
        this.setMinHeight(ApplicationSAE.height);
        this.setMinWidth(ApplicationSAE.width);
    }

    private void initUI() {

        try {

            BorderPane top = new BorderPane();
            top.setRight(new Label("Panier de " + client.getNom()));
            this.home = new Button("Home");
            this.home.setOnAction((ActionEvent) -> {
                this.app.getStage().setScene(new Scene(new ClientWindow(this.app)));
            });

            VBox btnHomeBox = new VBox(10, this.home);
            btnHomeBox.setAlignment(Pos.CENTER_RIGHT);
            VBox.setMargin(this.home, new Insets(20));

            top.setLeft(this.home);

            this.setTop(top);

            // center

            VBox center = new VBox(10);
            center.setAlignment(Pos.CENTER);

            for (Integer libID : client.getPanier().getContenu().keySet()) {

                Librairie lib = Reseau.getLibrairie(libID);

                Map<Livre, Integer> livres = client.getPanier().getContenu().get(libID);

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

                    ImageView bookImage = new ImageView(new Image(getClass().getResourceAsStream("/images/insertion_image.png")));
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
                    bookTitle.setPrefWidth(300);
                    Label bookPrice = new Label("Prix : " + book.getPrix() + "€");
                    bookPrice.setPrefWidth(100);
                    bookPrice.setAlignment(Pos.CENTER_RIGHT);
                    Label bookStock = new Label("Quantité commandé : " + quantite);
                    bookStock.setPrefWidth(100);
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

                this.setCenter(center);
            }
        }
        catch (LibraryNotFoundException e){

        }
    }

    private void initEmptyUI(){

        Text emptyText = new Text("Votre Panier est vide");
        emptyText.setFont(Font.font("arial",25));
        VBox textBox = new VBox(10,emptyText);
        textBox.setAlignment(Pos.CENTER);

        // deactivate btn

        this.commander.setDisable(true);
        this.previousPage.setDisable(true);
        this.nextPage.setDisable(true);

        this.setCenter(textBox);

        // bottom

        HBox bottom = new HBox(20,this.commander,this.previousPage,this.nextPage,this.home);
        HBox.setMargin(this.previousPage, new Insets(10,10,20,10));
        HBox.setMargin(this.nextPage, new Insets(10, 10, 20, 10));
        HBox.setMargin(this.commander, new Insets(10, 60, 20, 10));
        HBox.setMargin(this.home, new Insets(10, 10, 20, 60));
        bottom.setAlignment(Pos.CENTER);

        this.setBottom(bottom);
    }

}
