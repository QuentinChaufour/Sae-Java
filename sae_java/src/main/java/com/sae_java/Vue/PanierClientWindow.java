package com.sae_java.Vue;

import java.util.Map;

import com.sae_java.Vue.alert.BookInfoAlert;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
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

        HBox bottom = new HBox(20, this.commander, this.previousPage, this.nextPage, this.home);
        HBox.setMargin(this.previousPage, new Insets(10, 10, 20, 10));
        HBox.setMargin(this.nextPage, new Insets(10, 10, 20, 10));
        HBox.setMargin(this.commander, new Insets(10, 60, 20, 10));
        HBox.setMargin(this.home, new Insets(10, 10, 20, 60));
        bottom.setAlignment(Pos.CENTER);

        this.setBottom(bottom);

        VBox center = new VBox(10);
        center.setAlignment(Pos.CENTER);

        Accordion accordion = new Accordion();

        for (Integer libID : client.getPanier().getContenu().keySet()) {

            Map<Livre, Integer> livres = client.getPanier().getContenu().get(libID);

            try {
                accordion.getPanes().add(new LibPanierPanel(libID, this.app, livres));
            } catch (LibraryNotFoundException e) {
            }
        }

        accordion.getPanes().get(0).setExpanded(true);
        this.setCenter(accordion);
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


    public void majCenter(){
        Accordion accordion = new Accordion();

        for (Integer libID : client.getPanier().getContenu().keySet()) {

            Map<Livre, Integer> livres = client.getPanier().getContenu().get(libID);

            try {
                accordion.getPanes().add(new LibPanierPanel(libID, this.app, livres));
            } catch (LibraryNotFoundException e) {
            }
        }

        accordion.getPanes().get(0).setExpanded(true);
        this.setCenter(accordion);
    }
}
