package com.sae_java.Vue.client;

import java.util.List;

import com.sae_java.Modele.Livre;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.alert.BookInfoAlert;
import com.sae_java.Vue.controleur.ControleurAddRecommandation;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class RecommandationsWindow extends BorderPane {

    private final ApplicationSAE app;

    private Button home;
    private ChoiceBox<Livre> bookSelection;
    private Button moreInfo;
    private TextField qteSelector;
    private Button addBook;

    public RecommandationsWindow(ApplicationSAE app,List<Livre> recommandations) {
        super();
        this.app = app;
        this.home = new Button("Home");
        this.home.setOnAction((ActionEvent) -> {this.app.getStage().setScene(new Scene(new ClientWindow(this.app)));});
        
        this.bookSelection = new ChoiceBox<>(FXCollections.observableList(recommandations));
        this.moreInfo = new Button("+ INFO");
        // image bulle info ?

        this.qteSelector = new TextField();
        this.qteSelector.setPromptText("Quantité");
        this.addBook = new Button();
        this.addBook.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ajout_16px.png"))));

        //size 

        this.bookSelection.setPrefWidth(0.4 * ApplicationSAE.width);
        this.moreInfo.setPrefWidth(0.05 * ApplicationSAE.width);
        this.addBook.setPrefWidth(0.05 * ApplicationSAE.width);
        this.qteSelector.setPrefWidth(0.2 * ApplicationSAE.width);

        this.bookSelection.setPrefHeight(0.035 * ApplicationSAE.height);
        this.moreInfo.setPrefHeight(0.035 * ApplicationSAE.height);
        this.addBook.setPrefHeight(0.035 * ApplicationSAE.height);
        this.qteSelector.setPrefHeight(0.035 * ApplicationSAE.height);

        this.addBook.setOnAction(new ControleurAddRecommandation(this.app, this.bookSelection, this.qteSelector));
        this.moreInfo.setOnAction((ActionEvent) -> {
            Livre book = this.bookSelection.getValue();
            if(book.getImage() == null){
                new BookInfoAlert(book, new Image(getClass().getResourceAsStream("/images/insertion_image.png")));
            }
            else{
                new BookInfoAlert(book, book.getImage());
            }
        });

        HBox bottom = new HBox(20,this.bookSelection,this.moreInfo,this.qteSelector,this.addBook);
        bottom.setAlignment(Pos.CENTER);
        BorderPane.setMargin(bottom, new Insets(10,10,40,10));
        this.setBottom(bottom);

        if(recommandations.isEmpty()){
            this.initEmptyUI();
        }
        else{
            this.initUI(recommandations);
        }
        
        this.setMinHeight(ApplicationSAE.height);
        this.setMinWidth(ApplicationSAE.width);
    }

    private void initUI(List<Livre> recommandations) {

        HBox top = new HBox(10, this.home);
        top.setAlignment(Pos.CENTER_RIGHT);
        this.home.setFont(Font.font("arial", 22));

        this.setTop(top);

        this.bookSelection.setValue(recommandations.get(0));

        ScrollPane center = new ScrollPane();

        center.setPadding(new Insets(20));
        
        VBox centerContent = new VBox(10);
        centerContent.setAlignment(Pos.CENTER);

        for(Livre book : recommandations){
            HBox bookBox = new HBox(10);
            bookBox.setAlignment(Pos.CENTER_LEFT);
            Text bookTitle = new Text(book.getTitre());
            bookTitle.setFont(Font.font("arial", 20));
            if(book.getImage() != null) {
                ImageView bookImage = new ImageView(book.getImage());
                bookImage.setFitHeight(96);
                bookImage.setFitWidth(96);
                bookBox.getChildren().add(bookImage);
            } else {
                ImageView bookImage = new ImageView(new Image(getClass().getResourceAsStream("/images/insertion_image.png")));
                bookImage.setFitHeight(96);
                bookImage.setFitWidth(96);
                bookBox.getChildren().add(bookImage);
            }
            bookBox.getChildren().add(bookTitle);
            centerContent.getChildren().add(bookBox);
        }
        center.setContent(centerContent);
        this.setCenter(center);       
    }


    private void initEmptyUI(){

        Text emptyText = new Text("Pas de recommandations disponibles");
        emptyText.setFont(Font.font("arial",25));

        VBox btnHomeBox = new VBox(10,this.home);
        this.home.setFont(Font.font("arial",22));
        btnHomeBox.setAlignment(Pos.CENTER_RIGHT);
        VBox.setMargin(this.home, new Insets(20));
        VBox textBox = new VBox(10,emptyText);
        textBox.setAlignment(Pos.CENTER);

        VBox center = new VBox(10,btnHomeBox,textBox);
        this.setCenter(center);

        // bottom disabled
        this.moreInfo.setDisable(true);
        this.addBook.setDisable(true);
        this.qteSelector.setDisable(true);

        HBox bottom = new HBox(20,this.bookSelection,this.moreInfo,this.qteSelector,this.addBook);
        bottom.setAlignment(Pos.CENTER);
        BorderPane.setMargin(bottom, new Insets(10,10,40,10));
        this.setBottom(bottom);
    }
}
