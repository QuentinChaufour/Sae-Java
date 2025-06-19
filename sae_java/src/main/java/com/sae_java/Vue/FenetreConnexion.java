package com.sae_java.Vue;

import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Reseau;
import com.sae_java.Vue.controleur.ControleurConnexionAdmin;
import com.sae_java.Vue.controleur.ControleurConnexionClient;
import com.sae_java.Vue.controleur.ControleurConnexionVendeur;
import com.sae_java.Vue.controleur.ControleurQuitter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@SuppressWarnings("FieldMayBeFinal")
public class FenetreConnexion {

    private ApplicationSAE app;

    private BorderPane pane;

    private TextField userNameField;
    private TextField userFornameField;
    private PasswordField userMDPField;

    private TextField sellerNameField;
    private TextField sellerFornameField;
    private PasswordField sellerMDPField;

    private TextField adminNameField;
    private PasswordField passwordField;

    private ChoiceBox<Librairie> choiceBoxLibrairieUser;

    public FenetreConnexion(ApplicationSAE app) {

        this.app = app;

        this.userNameField = new TextField();
        Platform.runLater(() -> this.userNameField.requestFocus());
        this.userFornameField = new TextField();
        this.userMDPField = new PasswordField();
        
        this.sellerNameField = new TextField();
        this.sellerFornameField = new TextField();
        this.sellerMDPField = new PasswordField();

        this.adminNameField = new TextField();
        this.passwordField = new PasswordField();

        ObservableList<Librairie> libList = FXCollections.observableList(Reseau.librairies);

        this.choiceBoxLibrairieUser = new ChoiceBox<>();
        this.choiceBoxLibrairieUser.setItems(libList);
        if(!libList.isEmpty()){
            this.choiceBoxLibrairieUser.setValue(libList.get(0));
        }

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F2F2F2; -fx-padding: 10; -fx-hgap: 10; -fx-vgap: 10;");


        // https://coderscratchpad.com/javafx-accordion-creating-collapsible-ui-sections/ for more information on how to use the accordion with tilepanes
        // Init UI components
        Label titleLabel = new Label("Idenfifiez-vous en cliquant sur le rôle souhaité");
        titleLabel.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        HBox titleBox = new HBox(titleLabel);
        titleBox.setStyle("-fx-background-color: #c1e7ff; -fx-padding: 10; -fx-spacing: 10; -fx-alignment: center;");

        // top label
        root.setTop(titleBox);


        // Create an Accordion
        Accordion accordion = new Accordion();

        // Create identification VBox

        GridPane identificationClient = new GridPane(20, 10);

        identificationClient.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-spacing: 10;");

        identificationClient.add(new Label("Nom : "), 0, 0);
        identificationClient.add(this.userNameField, 1, 0);
        identificationClient.add(new Label("Prénom : "), 0, 1);
        identificationClient.add(this.userFornameField, 1, 1);
        identificationClient.add(new Label("Mot de passe : "), 0, 2);
        identificationClient.add(this.userMDPField, 1, 2);
        identificationClient.add(new Label("Librairie : "), 0, 3);
        identificationClient.add(this.choiceBoxLibrairieUser, 1, 3);

        Button buttonLogginClient = new Button("S'identifier");
        // identification possible uniquement si tous les champs sont remplis
        buttonLogginClient.disableProperty().bind(this.userNameField.textProperty().isEmpty(). or (this.userFornameField.textProperty().isEmpty()). or (this.userMDPField.textProperty().isEmpty()));
        buttonLogginClient.setOnAction(new ControleurConnexionClient(this, this.app));

        identificationClient.add(buttonLogginClient, 0, 4, 2, 1);
        GridPane.setHalignment(buttonLogginClient, HPos.CENTER);

        GridPane identificationVendeur = new GridPane(20,10);

        identificationVendeur.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-spacing: 10;");

        identificationVendeur.add(new Label("Nom : "),0,0);
        identificationVendeur.add(this.sellerNameField, 1, 0);
        identificationVendeur.add(new Label("Prénom : "), 0, 1);
        identificationVendeur.add(this.sellerFornameField, 1, 1);
        identificationVendeur.add(new Label("Mot de passe : "), 0, 2);
        identificationVendeur.add(this.sellerMDPField, 1, 2);

        Button buttonLogginVendeur = new Button("S'identifier");
        buttonLogginVendeur.disableProperty().bind(this.sellerNameField.textProperty().isEmpty(). or (this.sellerFornameField.textProperty().isEmpty()). or (this.sellerMDPField.textProperty().isEmpty()));
        buttonLogginVendeur.setOnAction(new ControleurConnexionVendeur(this, this.app));

        identificationVendeur.add(buttonLogginVendeur, 0, 4, 2, 1);
        GridPane.setHalignment(buttonLogginVendeur, HPos.CENTER);

        GridPane identificationAdmin = new GridPane(20,10);

        identificationAdmin.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 10; -fx-spacing: 10;");

        identificationAdmin.add(new Label("Nom : "),0,0);
        identificationAdmin.add(this.adminNameField, 1, 0);
        identificationAdmin.add(new Label("Mot de passe : "), 0, 1);
        identificationAdmin.add(this.passwordField, 1, 1);

        Button buttonLogginAdmin = new Button("S'identifier");
        buttonLogginAdmin.disableProperty().bind(this.adminNameField.textProperty().isEmpty(). or (this.passwordField.textProperty().isEmpty()));
        buttonLogginAdmin.setOnAction(new ControleurConnexionAdmin(this, this.app));

        identificationAdmin.add(buttonLogginAdmin, 0, 4, 2, 1);
        GridPane.setHalignment(buttonLogginAdmin, HPos.CENTER);

        // Create TitledPanes with content
        TitledPane paneClient = new TitledPane("Client", identificationClient);
        paneClient.setStyle("-fx-padding: 10; -fx-spacing: 10;-fx-background-radius:10");

        TitledPane paneVendeur = new TitledPane("Vendeur", identificationVendeur);
        paneVendeur.setStyle("-fx-padding: 10; -fx-spacing: 10;-fx-background-radius:10");

        TitledPane paneAdmin = new TitledPane("Admin", identificationAdmin);
        paneAdmin.setStyle("-fx-padding: 10; -fx-spacing: 10;-fx-background-radius:10");

        // Add TitledPanes to the Accordion
        accordion.getPanes().addAll(paneClient, paneVendeur, paneAdmin);
        accordion.setExpandedPane(paneClient);
        accordion.setMaxHeight(100);
        accordion.setStyle("-fx-background-color: #e9e9e9F2; -fx-padding: 40; -fx-spacing: 10;");
        BorderPane.setAlignment(accordion, Pos.TOP_CENTER);

        // Add the Accordion to the BorderPane
        root.setCenter(accordion);
        root.setStyle("-fx-background-color: #e9e9e9F2; -fx-padding: 10; -fx-hgap: 10; -fx-vgap: 10;");

        // bottom button to quit

        VBox bottomBox = new VBox();
        Button quitButton = new Button();
        quitButton.setGraphic(new ImageView(new Image(getClass().getResource("/images/quitter_32px.png").toString())));
        quitButton.setStyle("-fx-background-color: #ff947b; -fx-text-fill: #23242A; -fx-padding: 10; -fx-font-size: 14px; -fx-font-weight: bold;");
        quitButton.setText("Quitter");
        quitButton.setOnAction(new ControleurQuitter(this.app));

        VBox.setMargin(quitButton,new Insets(10, 0, 0, 0));
        bottomBox.getChildren().add(quitButton);
        bottomBox.setAlignment(Pos.CENTER);

        root.setBottom(bottomBox);

        this.pane = root;
    }

    public BorderPane getRoot() {
        return this.pane;
    }

    public String getUserNameField() {
        return this.userNameField.getText().trim();
    }

    public String getUserFornameField() {
        return this.userFornameField.getText().trim();
    }

    public String getUserMDpField() {
        return this.userMDPField.getText().trim();
    }

    public Integer getChoiceBoxLibrairieUser() {
        return this.choiceBoxLibrairieUser.getValue().getId();
    }

    public String getSellerNameField() {
        return this.sellerNameField.getText().trim();
    }

    public String getSellerFornameField() {
        return this.sellerFornameField.getText().trim();
    }

    public String getSellerDMPField() {
        return this.sellerMDPField.getText().trim();
    }

    public String getAdminNameField() {
        return this.adminNameField.getText().trim();
    }

    public String getPasswordField() {
        return this.passwordField.getText().trim();
    }

    public void errorClient(){
        this.userFornameField.setStyle("-fx-border-color: red; -fx-border-width: 1px");
        this.userNameField.setStyle("-fx-border-color: red; -fx-border-width: 1px");
        this.userMDPField.setStyle("-fx-border-color: red; -fx-border-width: 1px");
    }
}