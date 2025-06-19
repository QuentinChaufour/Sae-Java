package com.sae_java.Vue.client;
import java.util.ArrayList;
import java.util.List;

import com.sae_java.Modele.Client;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.controleur.ControleurAddBookToPanier;
import com.sae_java.Vue.controleur.ControleurDeconnexion;
import com.sae_java.Vue.controleur.ControleurPage;
import com.sae_java.Vue.controleur.ControleurRecommandation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ClientWindow extends BorderPane{

    private final ApplicationSAE app;
    private int page; 
    private int maxPage;

    private Label librairieLabel;
    private Button backPage;
    private Button nextPage;
    private Label pageLabel;


    public ClientWindow(ApplicationSAE app) {
        this.app = app;
        this.page = 1;

        Client client = this.app.getClient();

        try {
            int size = Reseau.getLibrairie(client.getLibrairie()).consulterStock().size();
            this.maxPage = size % 5 == 0 ? (int)size/5 : ((int)size/5)+1; 
        } 
        catch (LibraryNotFoundException e) {
            maxPage = 2;
        }
        
        this.minHeightProperty().set(ApplicationSAE.height);
        this.minWidthProperty().set(ApplicationSAE.width);

        // top of the borderPane

        BorderPane top = new BorderPane();
        
        // Create a button for "Deconnexion"
        Button deconnexion = new Button("Deconnexion");
        deconnexion.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/deconnexion_32px.png"))));
        deconnexion.setOnAction(new ControleurDeconnexion(this.app));

        // create a button for Panier 

        Button panierBtn = new Button("Panier");
        panierBtn.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/panier_32px.png"))));
        panierBtn.setOnAction((ActionEvent) -> {this.setPanierMenu();});

        deconnexion.setFont(Font.font(18));
        panierBtn.setFont(Font.font(18));

        BorderPane.setMargin(deconnexion, new Insets(15));
        BorderPane.setAlignment(deconnexion, Pos.CENTER_LEFT);
        BorderPane.setAlignment(panierBtn, Pos.CENTER_RIGHT);
        BorderPane.setMargin(panierBtn, new Insets(15));

        this.librairieLabel = new Label();
        try {
            this.librairieLabel.setText(Reseau.getLibrairie(app.getClient().getLibrairie()) + "");
            this.librairieLabel.setFont(Font.font("Arial",FontWeight.BOLD,30));
        } 
        catch (LibraryNotFoundException e) {

            // alert ?
        }

        if (ApplicationSAE.lightMode) {

            panierBtn.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #BCCCDC;-fx-background-radius:20");
            deconnexion.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #BCCCDC;-fx-background-radius: 20");

            panierBtn.setOnMouseEntered(e -> {panierBtn.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color :  #9AA6B2;-fx-background-radius: 20");});
            panierBtn.setOnMouseExited(e -> {panierBtn.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #BCCCDC;-fx-background-radius: 20");});

            deconnexion.setOnMouseEntered(e -> {deconnexion.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color :#ffa5a5;-fx-background-radius: 20");});
            deconnexion.setOnMouseExited(e -> {deconnexion.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #BCCCDC;-fx-background-radius: 20");});

            deconnexion.setPrefWidth(ApplicationSAE.width * 0.2);
            panierBtn.setPrefWidth(ApplicationSAE.width * 0.2);
        }


        top.setLeft(deconnexion);
        top.setCenter(librairieLabel);
        top.setRight(panierBtn);


        this.setTop(top);

        // left side of the borderPane

        // client infos

        HBox clientInfo = new HBox(10);
        clientInfo.setStyle("-fx-padding: 10; -fx-background-color: #D9EAFD; -fx-border-color: #000000; -fx-border-width: 2;");
        Label clientName = new Label(app.getClient().getNom());
        Label clientForname = new Label(app.getClient().getPrenom());
        ImageView clientImage = new ImageView(new Image(getClass().getResourceAsStream("/images/utilisateur.png")));

        clientImage.setFitHeight(32);
        clientImage.setFitWidth(32);
        clientImage.setPreserveRatio(true);
        clientImage.setSmooth(true);

        clientInfo.getChildren().addAll(clientImage, clientForname,clientName);
        clientInfo.setAlignment(Pos.CENTER);

        VBox left = new VBox(10);

        // buttons

        Button recommandation = new Button("Recommandation");
        recommandation.setOnAction(new ControleurRecommandation(this.app));

        Button changerInfos = new Button("Update infos");
        changerInfos.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/reglages_32px.png"))));

        VBox changeLibBox = new VBox(10);
        ObservableList<Librairie> libList = FXCollections.observableList(Reseau.librairies);

        ChoiceBox<Librairie >choiceLib = new ChoiceBox<>();
        choiceLib.setItems(libList);
        choiceLib.setValue(libList.get(0));
        Button commitBtn = new Button("Change");

        changeLibBox.getChildren().addAll(choiceLib, commitBtn);

        TitledPane changeLibPane = new TitledPane("Changer de librairie", changeLibBox);
        changeLibPane.setExpanded(false);

        // changer lib client et affichage
        commitBtn.setOnAction((ActionEvent) -> {
            client.setLibrairie(choiceLib.getValue().getId());
            this.librairieLabel.setText(choiceLib.getValue() + "");
            this.majPage();
            changeLibPane.setExpanded(false);
        });

        ToggleButton switchTheme = new ToggleButton("light mode");
        BorderPane.setAlignment(switchTheme, Pos.BOTTOM_CENTER);
        if(ApplicationSAE.lightMode){
            switchTheme.setStyle("-fx-background-color: #F9FAFC");
        }

        switchTheme.setOnAction((ActionEvent) -> {
            if(ApplicationSAE.lightMode){
                switchTheme.setStyle("-fx-background-color: #24232A;-fx-text-fill: #FFFFFF");
                ApplicationSAE.lightMode = false;
                switchTheme.setText("dark mode");
            }
            else{
                switchTheme.setStyle("-fx-background-color: #F9FAFC;-fx-text-fill: #000000");
                ApplicationSAE.lightMode = true;
                switchTheme.setText("light mode");
            }
        });

        VBox.setMargin(clientInfo, new Insets(10));
        VBox.setMargin(recommandation, new Insets(10));
        VBox.setMargin(changerInfos, new Insets(10));
        VBox.setMargin(changeLibPane, new Insets(10));

        VBox actions = new VBox(clientInfo,recommandation, changerInfos, changeLibPane);
        VBox.setVgrow(actions, Priority.ALWAYS);

        switchTheme.setPrefWidth(ApplicationSAE.width * 0.1);

        left.getChildren().addAll(actions,switchTheme);
        left.setPrefWidth(ApplicationSAE.width * 0.15);

        if(ApplicationSAE.lightMode){
            left.setStyle("-fx-margin: 15;-fx-padding: 15; -fx-background-color: #9AA6B2;-fx-background-radius: 0 20 0 0");
        }

        this.setLeft(left);
        // center of the borderPane

        VBox center = new VBox();
        try {
            center = this.createPage(this.page);
            if(ApplicationSAE.lightMode){
                center.setStyle("-fx-background-color : #F8FAFC");
            }
        } 
        catch (LibraryNotFoundException e) {
            // alert ?
        }

        this.setCenter(center);
        BorderPane.setMargin(center, new Insets(10,20,10,20));

    }

    public VBox createPage(int page) throws LibraryNotFoundException{

        List<Livre> livres = new ArrayList<>(this.app.getClient().consulterLivres().keySet());

        VBox center = new VBox(10);

        for(int i = ( page - 1)*5;i< page*5;i++){
            if (i < livres.size()) {
                HBox book = this.createBook(livres.get(i));
                HBox.setMargin(book, new Insets(20));
                center.getChildren().add(book);

            }
        }
        center.setAlignment(Pos.CENTER);

        HBox pageBox = new HBox(10);

        this.pageLabel = new Label(this.page + "");
        this.pageLabel.setStyle("-fx-font-size:20;-fx-font-weight:bold");

        this.backPage = new Button();
        this.backPage.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/left_arrow_32px.png"))));
        this.backPage.setPrefWidth(ApplicationSAE.width * 0.15);

        this.nextPage = new Button();
        this.nextPage.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/right_arrow_32px.png"))));
        this.nextPage.setPrefWidth(ApplicationSAE.width * 0.15);

        this.nextPage.setOnAction(new ControleurPage(this, this.app, this.backPage, this.nextPage));
        this.backPage.setOnAction(new ControleurPage(this, this.app, this.backPage, this.nextPage));

        pageBox.getChildren().addAll(this.backPage, pageLabel, this.nextPage);
        pageBox.setAlignment(Pos.CENTER);

        if(ApplicationSAE.lightMode){
            this.nextPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");
            this.backPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");

            this.nextPage.setOnMouseEntered(e -> {this.nextPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #9AA6B2;-fx-background-radius: 20");});
            this.nextPage.setOnMouseExited(e -> {this.nextPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");});

            this.backPage.setOnMouseEntered(e -> {this.backPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #9AA6B2;-fx-background-radius: 20");});
            this.backPage.setOnMouseExited(e -> {this.backPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");});

            this.pageLabel.setStyle("-fx-font-size : 18; -fx-font-weight: bold");
            pageBox.setStyle("-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");
            
            pageBox.setMaxWidth(ApplicationSAE.width * 0.4);
            HBox.setMargin(pageBox, new Insets(20,0,5,0));
        }

        BorderPane.setAlignment(pageBox, Pos.CENTER);
        VBox.setMargin(pageBox, new Insets(20,0,0,0));
        center.getChildren().add(pageBox);

        center.setPrefHeight(ApplicationSAE.height * 0.85);
        return center;
    }

    private HBox createBook(Livre book) throws LibraryNotFoundException{
        HBox bookBox = new HBox(10);
        HBox infosBookBox = new HBox(10);
        infosBookBox.setPrefWidth(ApplicationSAE.width * 0.6);
        bookBox.setPrefWidth(ApplicationSAE.width * 0.7);
        infosBookBox.setAlignment(Pos.CENTER_LEFT);
        
        if(ApplicationSAE.lightMode){
            bookBox.setStyle("-fx-margin:5 ;-fx-padding: 10;-fx-background-color: #F8FAFC");
            bookBox.setEffect(new DropShadow(5, Color.GRAY));
            infosBookBox.setStyle("-fx-margin:10 ;-fx-padding: 10; -fx-background-color: #F8FAFC");
        }

        ImageView bookImage;

        if (book.getImage() == null) {
            bookImage = new ImageView(new Image(getClass().getResourceAsStream("/images/insertion_image.png")));
            bookImage.setFitHeight(96);
            bookImage.setFitWidth(96);
            bookImage.setPreserveRatio(true);
            bookImage.setSmooth(true);
        }
        else{
            bookImage = new ImageView(book.getImage());
            bookImage.setFitHeight(96);
            bookImage.setFitWidth(96);
            bookImage.setPreserveRatio(true);
            bookImage.setSmooth(true);
        }

        String bookName = book.getTitre();

        if(bookName.length() > 60){
            bookName = bookName.substring(0, 60);
            bookName += " ...";
        }

        Label bookTitle = new Label(bookName);
        bookTitle.setPrefWidth(ApplicationSAE.width * 0.3);
        Label bookPrice = new Label("Prix : " + book.getPrix() + " €");
        bookPrice.setPrefWidth(ApplicationSAE.width * 0.1);
        bookPrice.setAlignment(Pos.CENTER_RIGHT);
        Label bookStock = new Label("Stock : " + Reseau.getLibrairie(this.app.getClient().getLibrairie()).consulterStock().get(book));
        bookStock.setPrefWidth(ApplicationSAE.width * 0.1);
        bookStock.setAlignment(Pos.CENTER_RIGHT);

        bookTitle.setStyle("-fx-font-size: 20px;");
        bookPrice.setStyle("-fx-font-size: 18px;");
        bookStock.setStyle("-fx-font-size: 18px;");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantité");
        quantityField.setPrefWidth(ApplicationSAE.width * 0.1);
        //quantityField.setAlignment(Pos.CENTER_LEFT);

        Button addToPanier = new Button("Ajouter au panier");
        addToPanier.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ajout_16px.png"))));
        addToPanier.setAlignment(Pos.CENTER_RIGHT);
        addToPanier.setOnAction(new ControleurAddBookToPanier(this.app, book, quantityField));
        addToPanier.setPrefWidth(ApplicationSAE.width * 0.1);
        addToPanier.setStyle("-fx-background-color: #9AA6B2");

        HBox.setMargin(quantityField, new Insets(5));
        HBox.setMargin(addToPanier, new Insets(5));
        // pop up si nb négatif ou not a number quand ajouter au panier

        infosBookBox.getChildren().addAll(bookImage,bookTitle, bookPrice, bookStock);
        infosBookBox.setAlignment(Pos.CENTER_LEFT);

        bookBox.getChildren().addAll(infosBookBox, quantityField,addToPanier);
        bookBox.setAlignment(Pos.CENTER);

        return bookBox;
    }

    public int getPage(){return this.page;}
    public void IncPage(){
        if(this.page < maxPage){
            this.page++;
        }
    }

    public void RedcPage() {
        if (this.page > 1) {
            this.page--;
        }
    }

    public void majPage(){
        VBox center;
        try {
            center = this.createPage(this.page);
            this.setCenter(center);
            BorderPane.setMargin(center, new Insets(10,20,10,20));
            this.pageLabel.setText(this.page + "");
        } 
        catch (LibraryNotFoundException e) {
            
            // alert ?
        }
    }

    public void setMainMenu(){
        this.app.getStage().setScene(new Scene(this));
    }

    public void setPanierMenu(){
        this.app.getStage().setScene(new Scene(new PanierClientWindow(this.app))); 
    }
}
