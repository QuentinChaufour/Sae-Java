package com.sae_java.Vue;

import java.util.List;

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

import com.sae_java.Modele.Livre;

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

        GridPane center = new GridPane();
        int nbRecommandation = recommandations.size();
        
        // amélioration : for qui selon la parité place crée le podium sauf 1 et 10
        switch (nbRecommandation) {
            case 1:
                center.add(new Text(), 0, 0);
                center.add(new Text("1. " + recommandations.get(0).getTitre()), 1, 0);
                center.add(new Text(), 2, 0);

            case 2:
                center.add(new Text("2. " + recommandations.get(1).getTitre()), 0, 1);

            case 3:
                center.add(new Text(), 1, 1);
                center.add(new Text("3. " + recommandations.get(2).getTitre()), 2, 1);
        
            case 4:
                center.add(new Text("4. " + recommandations.get(3).getTitre()), 0, 2);

            case 5:
                center.add(new Text(), 1, 2);
                center.add(new Text("" + recommandations.get(0).getTitre()), 2, 2);

            case 6:
                center.add(new Text("6. " + recommandations.get(5).getTitre()), 0, 3);

            case 7:
                center.add(new Text(), 1, 3);
                center.add(new Text("7. " + recommandations.get(6).getTitre()), 2, 3);

            case 8:
                center.add(new Text("8. " + recommandations.get(7).getTitre()), 0, 4);

            case 9:

                center.add(new Text(), 1, 4);
                center.add(new Text("9. " + recommandations.get(8).getTitre()), 2, 4);

            case 10:
                center.add(new Text(), 0, 5);
                center.add(new Text("10.    " + recommandations.get(0).getTitre()), 1,5);
                center.add(new Text(), 2, 5);

            default:
                break;
                
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
