package com.sae_java.Vue;
import java.util.ArrayList;
import java.util.List;

import com.sae_java.Modele.Client;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ClientWindow extends BorderPane{

    private final ApplicationSAE app;
    private int page; 
    private int maxPage;

    private Label librairieLabel;
    private Button backPage;
    private Button nextPage;


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

        Button panier = new Button("Panier");
        panier.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/panier_32px.png"))));
        panier.setStyle("-fx-margin: 10; -fx-padding: 10;");
        panier.setOnAction((ActionEvent) -> {this.app.getStage().setScene(new Scene(new PanierClientWindow(this.app)));});

        BorderPane.setMargin(top, new Insets(0, 0, 10, 0));
        BorderPane.setMargin(deconnexion, new Insets(5));
        BorderPane.setAlignment(deconnexion, Pos.CENTER_LEFT);
        BorderPane.setAlignment(panier, Pos.CENTER_RIGHT);
        BorderPane.setMargin(panier, new Insets(5));

        this.librairieLabel = new Label();
        try {
            this.librairieLabel.setText(Reseau.getLibrairie(app.getClient().getLibrairie()) + "");
            this.librairieLabel.setFont(Font.font("Arial",FontWeight.BOLD,30));
        } 
        catch (LibraryNotFoundException e) {

            // alert ?

            e.printStackTrace();
        }

        top.setLeft(deconnexion);
        top.setCenter(librairieLabel);
        top.setRight(panier);

        this.setTop(top);

        // left side of the borderPane

        // client infos

        HBox clientInfo = new HBox(10);
        clientInfo.setStyle("-fx-padding: 10; -fx-background-color: #d7fffb; -fx-border-color: #000000; -fx-border-width: 2;");
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

        Button parametres = new Button();
        parametres.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/reglages_32px.png"))));

        left.getChildren().addAll(clientInfo,recommandation, changerInfos, changeLibPane,parametres);
        left.setStyle("-fx-margin: 10;-fx-padding: 10; -fx-background-color: #ffcae2;");
        this.setLeft(left);
        // center of the borderPane

        VBox center = null;
        try {
            center = this.createPage(this.page);
        } 
        catch (LibraryNotFoundException e) {

            // alert ?

            e.printStackTrace();
        }

        this.setCenter(center);

        // bottom of the borderPane
        HBox bottom = new HBox(10);
        Label credit = new Label("V1 par Quentin");
        bottom.getChildren().add(credit);

        this.setBottom(bottom);

    }

    public VBox createPage(int page) throws LibraryNotFoundException{

        List<Livre> livres = new ArrayList<>(this.app.getClient().consulterLivres().keySet());

        VBox center = new VBox(10);

        for(int i = ( page - 1)*5;i< page*5;i++){
            if(i<livres.size()){
                HBox book = this.createBook(livres.get(i));
                center.getChildren().add(book);

            }
        }

        HBox pageBox = new HBox(10);

        Label pages = new Label(this.page + "");

        this.backPage = new Button();
        this.backPage.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/left_arrow_32px.png"))));

        this.nextPage = new Button();
        this.nextPage.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/right_arrow_32px.png"))));
        this.nextPage.setOnAction(new ControleurPage(this, this.app, this.backPage, this.nextPage));
        this.backPage.setOnAction(new ControleurPage(this, this.app, this.backPage, this.nextPage));

        pageBox.getChildren().addAll(this.backPage, pages, this.nextPage);
        pageBox.setStyle("-fx-padding: 10;");

        pageBox.setAlignment(Pos.CENTER);
        center.getChildren().add(pageBox);

        return center;
    }

    private HBox createBook(Livre book) throws LibraryNotFoundException{
        HBox bookBox = new HBox(10);
        HBox infosBookBox = new HBox(10);
        infosBookBox.setPrefWidth(750);
        infosBookBox.setAlignment(Pos.CENTER_LEFT);
        bookBox.setStyle("-fx-margin:10 ;-fx-padding: 15;");
        infosBookBox.setStyle("-fx-margin:10 ;-fx-padding: 10; -fx-background-color: #d7fffb;");

        ImageView bookImage = new ImageView(new Image(getClass().getResourceAsStream("/images/insertion_image.png")));
        bookImage.setFitHeight(64);
        bookImage.setFitWidth(64);
        bookImage.setPreserveRatio(true);
        bookImage.setSmooth(true);

        String bookName = book.getTitre();

        if(bookName.length() > 30){
            bookName = bookName.substring(0, 30);
            bookName += "...";
        }

        Label bookTitle = new Label("Titre : " + bookName);
        bookTitle.setPrefWidth(300);
        Label bookPrice = new Label("Prix : " + book.getPrix() + "€");
        bookPrice.setPrefWidth(100);
        bookPrice.setAlignment(Pos.CENTER_RIGHT);
        Label bookStock = new Label("Stock : " + Reseau.getLibrairie(this.app.getClient().getLibrairie()).consulterStock().get(book));
        bookStock.setPrefWidth(100);
        bookStock.setAlignment(Pos.CENTER_RIGHT);

        bookTitle.setStyle("-fx-font-size: 16px;");
        bookPrice.setStyle("-fx-font-size: 14px;");
        bookStock.setStyle("-fx-font-size: 14px;");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantité");
        quantityField.setPrefWidth(100);
        //quantityField.setAlignment(Pos.CENTER_LEFT);

        Button addToPanier = new Button("Ajouter au panier");
        addToPanier.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ajout_16px.png"))));
        addToPanier.setAlignment(Pos.CENTER_RIGHT);
        addToPanier.setOnAction(new ControleurAddBookToPanier(this.app, book, quantityField));

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
        VBox page;
        try {
            page = this.createPage(this.page);
            this.setCenter(page);
        } 
        catch (LibraryNotFoundException e) {
            
            // alert ?
            e.printStackTrace();
        }
    }

}
