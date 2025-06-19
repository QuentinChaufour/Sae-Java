package com.sae_java.Vue;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import com.sae_java.Modele.Vendeur;
import com.sae_java.Vue.controleur.*;
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;

public class VendeurWindow extends BorderPane {

    private final ApplicationSAE app;
    private final Vendeur vendeur;

    public VendeurWindow(ApplicationSAE app, Vendeur vendeur) {
        this.app = app;
        this.vendeur = vendeur;

        this.setMinHeight(600);
        this.setMinWidth(1200);
        this.setStyle("-fx-font-family: 'Segoe UI'; -fx-background-color: linear-gradient(to bottom right, #f4f4f4, #e0e0e0);");

        // TOP BAR
        BorderPane top = new BorderPane();
        top.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-border-color: #e0e0e0; -fx-border-width: 0 0 1px 0;");
        Button deconnexion = new Button("Déconnexion");
        deconnexion.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 15; -fx-cursor: hand;");
        deconnexion.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/deconnexion_32px.png"))));
        deconnexion.setOnAction(new ControleurDeconnexion(this.app));
        top.setLeft(deconnexion);
        BorderPane.setAlignment(deconnexion, Pos.CENTER_LEFT);
        BorderPane.setMargin(deconnexion, new Insets(5));

        //Titre
        Label titre = new Label("Livre - Express");
        titre.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-text-fill: #333;");
        top.setCenter(titre);
        BorderPane.setAlignment(titre, Pos.CENTER);

        //LOGO
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
        logo.setFitHeight(70); 
        logo.setPreserveRatio(true);
        top.setRight(logo);
        BorderPane.setAlignment(logo, Pos.CENTER_RIGHT);
        BorderPane.setMargin(logo, new Insets(5));
        this.setTop(top);

        
        // LEFT BAR - Infos vendeur
        VBox left = new VBox(15);
        left.setStyle("-fx-background-color: #fafafa; -fx-padding: 15; -fx-border-color: #e0e0e0; -fx-border-width: 0 1px 0 0;");
        
        HBox vendeurInfo = new HBox(10);
        vendeurInfo.setStyle("-fx-padding: 10; -fx-background-color: #ffffff; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 3);");
        Label vendeurName = new Label(this.vendeur.getNom());
        Label vendeurForname = new Label(this.vendeur.getPrenom());
        ImageView vendeurImage = new ImageView(new Image(getClass().getResourceAsStream("/images/utilisateur.png")));
        vendeurImage.setFitHeight(32);
        vendeurImage.setFitWidth(32);
        vendeurInfo.getChildren().addAll(vendeurImage, vendeurName, vendeurForname);

        Label librairieLabel = new Label();
        try {
            librairieLabel.setText("Librairie : " + Reseau.getLibrairie(this.vendeur.getIdLibrairie()));
        } catch (LibraryNotFoundException e) {
            e.printStackTrace();
        }

        Button changerInfos = new Button("Update infos");
        changerInfos.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        changerInfos.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/reglages_32px.png"))));

        left.getChildren().addAll(vendeurInfo, librairieLabel, changerInfos);
        this.setLeft(left);

        // CENTER - Actions
        VBox center = new VBox(15);
        center.setPadding(new Insets(20));

        // Fonction utilitaire pour le style des titres
        String sectionStyle = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #444; -fx-padding: 0 0 5 0;";
        String boxStyle = "-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0, 0, 2);";

        // Ajouter un livre
        VBox addLivre = new VBox(10);
        addLivre.setStyle(boxStyle);
        Label addLivreLabel = new Label("Ajouter un livre au stock");
        addLivreLabel.setStyle(sectionStyle);
        HBox addLivreInput = new HBox(10);
        TextField addIsbn = new TextField(); addIsbn.setPromptText("ISBN");
        TextField addQuantite = new TextField(); addQuantite.setPromptText("Quantité");
        Button addLivreBtn = new Button("Ajouter");
        styliserChamp(addIsbn); styliserChamp(addQuantite); styliserBouton(addLivreBtn);
        addLivreInput.getChildren().addAll(new Label("ISBN :"), addIsbn, new Label("Quantité :"), addQuantite, addLivreBtn);
        addLivre.getChildren().addAll(addLivreLabel, addLivreInput);
        addLivreBtn.setOnAction(new ControleurAjouterLivre(app, this, addIsbn, addQuantite));

        // Modifier la quantité
        VBox updateStock = new VBox(10);
        updateStock.setStyle(boxStyle);
        Label updateLabel = new Label("Mettre à jour la quantité d’un livre");
        updateLabel.setStyle(sectionStyle);
        HBox updateInput = new HBox(10);
        TextField updateIsbn = new TextField(); updateIsbn.setPromptText("ISBN");
        TextField updateQuantite = new TextField(); updateQuantite.setPromptText("Nouvelle quantité");
        Button updateBtn = new Button("Actualiser");
        styliserChamp(updateIsbn); styliserChamp(updateQuantite); styliserBouton(updateBtn);
        updateInput.getChildren().addAll(new Label("ISBN :"), updateIsbn, new Label("Quantité :"), updateQuantite, updateBtn);
        updateStock.getChildren().addAll(updateLabel, updateInput);
        updateBtn.setOnAction(new ControleurUpdateQuantite(app, this, updateIsbn, updateQuantite));

        // Vérifier disponibilité
        VBox checkDispo = new VBox(10);
        checkDispo.setStyle(boxStyle);
        Label checkLabel = new Label("Vérifier la disponibilité d’un livre");
        checkLabel.setStyle(sectionStyle);
        HBox checkInput = new HBox(10);
        TextField checkIsbn = new TextField(); checkIsbn.setPromptText("ISBN");
        Button checkBtn = new Button("Vérifier");
        styliserChamp(checkIsbn); styliserBouton(checkBtn);
        checkInput.getChildren().addAll(new Label("ISBN :"), checkIsbn, checkBtn);
        checkDispo.getChildren().addAll(checkLabel, checkInput);
        checkBtn.setOnAction(new ControleurCheckDisponibilite(app, this, checkIsbn));

        // Passer une commande client
        VBox commandeClient = new VBox(10);
        commandeClient.setStyle(boxStyle);
        Label commandeLabel = new Label("Passer une commande pour un client");
        commandeLabel.setStyle(sectionStyle);
        HBox commandeInput = new HBox(10);
        TextField commandeClientId = new TextField(); commandeClientId.setPromptText("ID Client");
        TextField commandeIsbn = new TextField(); commandeIsbn.setPromptText("ISBN");
        TextField commandeQuantite = new TextField(); commandeQuantite.setPromptText("Quantité");
        Button commandeBtn = new Button("Commander");
        styliserChamp(commandeClientId); styliserChamp(commandeIsbn); styliserChamp(commandeQuantite); styliserBouton(commandeBtn);
        commandeInput.getChildren().addAll(new Label("Client ID :"), commandeClientId, new Label("ISBN :"), commandeIsbn, new Label("Quantité :"), commandeQuantite, commandeBtn);
        commandeClient.getChildren().addAll(commandeLabel, commandeInput);
        commandeBtn.setOnAction(new ControleurPasserCommande(app, this, commandeClientId, commandeIsbn, commandeQuantite));

        // Transfert de livre
        VBox transfert = new VBox(10);
        transfert.setStyle(boxStyle);
        Label transfertLabel = new Label("Transférer un livre depuis une autre librairie");
        transfertLabel.setStyle(sectionStyle);
        HBox transfertInput = new HBox(10);

        ObservableList<Librairie> listLib = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> sourceLibCombo = new ChoiceBox<>();
        sourceLibCombo.setItems(listLib);
        if (!listLib.isEmpty()) sourceLibCombo.setValue(listLib.get(0));
        styliserChamp(sourceLibCombo);

        TextField transfertIsbn = new TextField(); transfertIsbn.setPromptText("ISBN");
        TextField transfertQuantite = new TextField(); transfertQuantite.setPromptText("Quantité");
        Button transfertBtn = new Button("Transférer");
        styliserChamp(transfertIsbn); styliserChamp(transfertQuantite); styliserBouton(transfertBtn);
        transfertInput.getChildren().addAll(new Label("Librairie source :"), sourceLibCombo, new Label("ISBN :"), transfertIsbn, new Label("Quantité :"), transfertQuantite, transfertBtn);
        transfert.getChildren().addAll(transfertLabel, transfertInput);
        transfertBtn.setOnAction(new ControleurTransfererLivVen(app,this,transfertIsbn,sourceLibCombo, transfertQuantite));

        center.getChildren().addAll(addLivre, updateStock, checkDispo, commandeClient, transfert);
        this.setCenter(center);

        // BOTTOM
        HBox bottom = new HBox();
        bottom.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Label credit = new Label("V1 par Jolan et River");
        credit.setStyle("-fx-font-size: 12px; -fx-text-fill: #888;");
        bottom.getChildren().add(credit);
        this.setBottom(bottom);
    }

    private void styliserChamp(Control champ) {
        champ.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 5;");
    }

    private void styliserBouton(Button bouton) {
        bouton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 5 15; -fx-cursor: hand;");
    }

    public Vendeur getVendeur() {
        return this.vendeur;
    }
}
