package com.sae_java.Vue.client;

import java.util.List;

import com.sae_java.Modele.Livre;
import com.sae_java.Vue.ApplicationSAE;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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

        GridPane center = new GridPane(10,10);
        int nbRecommandation = recommandations.size();
        center.setAlignment(Pos.CENTER);
        center.setPadding(new Insets(20));
        
        // amélioration : for qui selon la parité place crée le podium sauf 1 et 10
        for(int i=1; i<=nbRecommandation;i++){

            if(i == 1){
                Text first = new Text("1. " + recommandations.get(0).getTitre());
                first.setTextAlignment(TextAlignment.CENTER);
                center.add(first, 0, 0,3,1);
                first.setFont(Font.font("arial",20));
            }
            else if(i == 10){
                Text last = new Text("10. " + recommandations.get(0).getTitre());
                center.add(last, 1,5,3,1);
                last.setFont(Font.font("arial",15));
            }
            else if(i%2 == 0){
                center.add(new Text(i+". " + recommandations.get(i-1).getTitre()), 0, i-1);
            }
            else{
                center.add(new Text(), 1, i-1);
                center.add(new Text(i+". " + recommandations.get(i-1).getTitre()), 2, i-1);
            }
        }
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
