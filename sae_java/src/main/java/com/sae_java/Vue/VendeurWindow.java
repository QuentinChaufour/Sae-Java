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
        
        // TOP BAR
        BorderPane top = new BorderPane();
        Button deconnexion = new Button("Deconnexion");
        deconnexion.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/deconnexion_32px.png"))));
        deconnexion.setOnAction(new ControleurDeconnexion(this.app));
        BorderPane.setMargin(deconnexion, new Insets(5));
        BorderPane.setAlignment(deconnexion, Pos.CENTER_LEFT);
        top.setLeft(deconnexion);
        this.setTop(top);

        // LEFT BAR - Info Vendeur
        HBox vendeurInfo = new HBox(10);
        vendeurInfo.setStyle("-fx-padding: 10; -fx-background-color: #d7fffb; -fx-border-color: #000000; -fx-border-width: 2;");
        Label vendeurName = new Label(app.getVendeur().getNom());
        Label vendeurForname = new Label(app.getVendeur().getPrenom());
        ImageView vendeurImage = new ImageView(new Image(getClass().getResourceAsStream("/images/utilisateur.png")));
        vendeurImage.setFitHeight(32);
        vendeurImage.setFitWidth(32);
        vendeurInfo.getChildren().addAll(vendeurImage, vendeurName, vendeurForname);

        VBox left = new VBox(10);
        Label librairieLabel = new Label();
        try {
            librairieLabel.setText("Librairie : " + Reseau.getLibrairie(app.getVendeur().getIdLibrairie()));
        } catch (LibraryNotFoundException e) {
            e.printStackTrace();
        }
        Button changerInfos = new Button("Update infos");
        changerInfos.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/reglages_32px.png"))));
        left.getChildren().addAll(vendeurInfo, librairieLabel, changerInfos);
        left.setStyle("-fx-padding: 10; -fx-background-color: #ffcae2;");
        this.setLeft(left);

        // CENTER - Actions
        VBox center = new VBox(15);

        // Ajouter un livre
        VBox addLivre = new VBox(5);
        addLivre.setPadding(new Insets(10));
        addLivre.setStyle("-fx-background-color: #d7fffb;");
        Label addLivreLabel = new Label("Ajouter un livre au stock");
        HBox addLivreInput = new HBox(10);
        TextField addIsbn = new TextField();
        addIsbn.setPromptText("ISBN");
        TextField addQuantite = new TextField();
        addQuantite.setPromptText("Quantité");
        Button addLivreBtn = new Button("Ajouter");
        addLivreInput.getChildren().addAll(new Label("ISBN :"), addIsbn, new Label("Quantité :"), addQuantite, addLivreBtn);
        addLivre.getChildren().addAll(addLivreLabel, addLivreInput);

        // Modifier quantité
        VBox updateStock = new VBox(5);
        updateStock.setPadding(new Insets(10));
        updateStock.setStyle("-fx-background-color: #d7fffb;");
        Label updateLabel = new Label("Mettre à jour la quantité d’un livre");
        HBox updateInput = new HBox(10);
        TextField updateIsbn = new TextField();
        updateIsbn.setPromptText("ISBN");
        TextField updateQuantite = new TextField();
        updateQuantite.setPromptText("Nouvelle quantité");
        Button updateBtn = new Button("Actualiser");
        updateInput.getChildren().addAll(new Label("ISBN :"), updateIsbn, new Label("Quantité :"), updateQuantite, updateBtn);
        updateStock.getChildren().addAll(updateLabel, updateInput);

        // Vérifier disponibilité
        VBox checkDispo = new VBox(5);
        checkDispo.setPadding(new Insets(10));
        checkDispo.setStyle("-fx-background-color: #d7fffb;");
        Label checkLabel = new Label("Vérifier la disponibilité d’un livre");
        HBox checkInput = new HBox(10);
        TextField checkIsbn = new TextField();
        checkIsbn.setPromptText("ISBN");
        Button checkBtn = new Button("Vérifier");
        checkInput.getChildren().addAll(new Label("ISBN :"), checkIsbn, checkBtn);
        checkDispo.getChildren().addAll(checkLabel, checkInput);

        // Passer une commande client
        VBox commandeClient = new VBox(5);
        commandeClient.setPadding(new Insets(10));
        commandeClient.setStyle("-fx-background-color: #d7fffb;");
        Label commandeLabel = new Label("Passer une commande pour un client");
        HBox commandeInput = new HBox(10);
        TextField commandeClientId = new TextField();
        commandeClientId.setPromptText("ID Client");
        TextField commandeIsbn = new TextField();
        commandeIsbn.setPromptText("ISBN");
        TextField commandeQuantite = new TextField();
        commandeQuantite.setPromptText("Quantité");
        Button commandeBtn = new Button("Commander");
        commandeInput.getChildren().addAll(new Label("Client ID :"), commandeClientId, new Label("ISBN :"), commandeIsbn, new Label("Quantité :"), commandeQuantite, commandeBtn);
        commandeClient.getChildren().addAll(commandeLabel, commandeInput);

        // Transfert depuis une autre librairie
        VBox transfert = new VBox(5);
        transfert.setPadding(new Insets(10));
        transfert.setStyle("-fx-background-color: #d7fffb;");
        Label transfertLabel = new Label("Transférer un livre depuis une autre librairie");
        HBox transfertInput = new HBox(10);
        TextField sourceLib = new TextField();
        sourceLib.setPromptText("ID Librairie source");
        TextField transfertIsbn = new TextField();
        transfertIsbn.setPromptText("ISBN");
        TextField transfertQuantite = new TextField();
        transfertQuantite.setPromptText("Quantité");
        Button transfertBtn = new Button("Transférer");
        transfertInput.getChildren().addAll(new Label("Librairie source :"), sourceLib, new Label("ISBN :"), transfertIsbn, new Label("Quantité :"), transfertQuantite, transfertBtn);
        transfert.getChildren().addAll(transfertLabel, transfertInput);

        // Ajouter tout au center
        center.getChildren().addAll(addLivre, updateStock, checkDispo, commandeClient, transfert);
        this.setCenter(center);

        // BOTTOM
        HBox bottom = new HBox(10);
        Label credit = new Label("V1 par Jolan");
        bottom.getChildren().add(credit);
        this.setBottom(bottom);
    }


    public Vendeur getVendeur() {
        return this.vendeur;
    }

}
