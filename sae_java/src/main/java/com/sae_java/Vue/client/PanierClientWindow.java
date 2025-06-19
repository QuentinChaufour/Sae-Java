package com.sae_java.Vue.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sae_java.Modele.Client;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Livre;
import com.sae_java.Vue.ApplicationSAE;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class PanierClientWindow extends BorderPane{
    

    private final ApplicationSAE app;
    private final Client client;

    private Button home;
    private Button commander;
    private Button previousPage;
    private Button nextPage;
    private Label prixTot;
    private Label nbElementPanier;
    private Label pageLabel;

    private int page;
    private int maxPage;

    public PanierClientWindow(ApplicationSAE app) {
        super();
        this.app = app;
        this.client = this.app.getClient();
        this.page = 1;
        this.maxPage = client.getPanier().getContenu().keySet().size();

        this.setStyle("-fx-background-color : #F5F5F5");
        this.setPrefSize(ApplicationSAE.width, ApplicationSAE.height);

        // init btn
        this.commander = new Button("Commander");
        this.commander.setFont(Font.font("arial", 22));
        this.commander.setOnAction((ActionEvent) -> {this.setCommandeMenu();});
        this.home = new  Button("Home");
        this.home.setFont(Font.font("arial", 22));
        this.home.setOnAction((ActionEvent) -> {this.app.getStage().setScene(new Scene(new ClientWindow(app)));});

        this.previousPage = new Button();
        this.previousPage.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/left_arrow_32px.png"))));
        this.previousPage.setOnAction(ActionEvent ->  {
            this.RedcPage();
            this.majCenter();
        });

        this.nextPage = new Button();
        this.nextPage.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/right_arrow_32px.png"))));
        this.nextPage.setOnAction(ActionEvent -> {
            this.IncPage();
            this.majCenter();
            ;
        });

        this.pageLabel = new Label(this.page +"");

        // size

        this.commander.setPrefWidth(0.2 * ApplicationSAE.width);
        this.home.setPrefWidth(0.2 * ApplicationSAE.width);
        this.previousPage.setPrefWidth(0.05 * ApplicationSAE.width);
        this.nextPage.setPrefWidth(0.05 * ApplicationSAE.width);

        this.commander.setPrefHeight(0.035 * ApplicationSAE.height);
        this.home.setPrefHeight(0.035 * ApplicationSAE.height);
        this.previousPage.setPrefHeight(0.035 * ApplicationSAE.height);
        this.nextPage.setPrefHeight(0.035 * ApplicationSAE.height);

        this.prixTot = new Label();
        this.nbElementPanier = new Label();

        if(this.client.getPanier().getContenu().isEmpty()){
            this.initEmptyUI();
        }
        else{
            this.initUI();
        }
    }

    private void initUI() {

        VBox center = new VBox(10);
        center.setAlignment(Pos.CENTER);

        Accordion accordion = new Accordion();
        boolean expanded = true;
        List<Integer> libs = new ArrayList<>(client.getPanier().getContenu().keySet());

        for (int i = (this.page - 1) * 5; i < maxPage * 5; i++) {

            if (i < libs.size()) {
                Integer libID = libs.get(i);
                Map<Livre, Integer> livres = client.getPanier().getContenu().get(libID);

                try {
                    TitledPane pane = new LibPanierPanel(libID, this.app,this, livres);
                    if (expanded) {
                        Platform.runLater(() -> pane.setExpanded(true));
                        expanded = false;
                    }
                    accordion.getPanes().add(pane);
                } catch (LibraryNotFoundException e) {
                }
            }
        }

        accordion.getPanes().get(0).setExpanded(true);
        accordion.setPrefHeight(ApplicationSAE.height * 0.8);
        accordion.setStyle("-fx-background-color: #e9e9e9F2; -fx-padding: 40; -fx-spacing: 10;");

        this.prixTot.setText("Prix total du panier : " + this.app.getClient().getPanier().getPrixTotal() + " €");
        this.prixTot.setStyle("-fx-font-size: 20");
        this.nbElementPanier.setText(this.app.getClient().getPanier().getNbElements() + " Livres dans la commande");
        this.nbElementPanier.setStyle("-fx-font-size: 17");

        VBox infoPanier = new VBox(10,this.nbElementPanier,this.prixTot);
        VBox.setMargin(this.nbElementPanier, new Insets(10,10,40,10));
        VBox.setMargin(this.prixTot, new Insets(10, 10, 40, 10));
        infoPanier.setAlignment(Pos.CENTER_RIGHT);

        VBox centerBox = new VBox(10,accordion,infoPanier);
        this.setCenter(centerBox);

        // bottom

        this.commander.setDisable(false);
        this.previousPage.setDisable(false);
        this.nextPage.setDisable(false);

        HBox pageBox = new HBox(10);

        this.pageLabel = new Label(this.page + "");
        this.pageLabel.setStyle("-fx-font-size:20;-fx-font-weight:bold");

        pageBox.getChildren().addAll(this.previousPage, pageLabel, this.nextPage);
        pageBox.setAlignment(Pos.CENTER);

        if(ApplicationSAE.lightMode){
            this.nextPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");
            this.previousPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");

            this.nextPage.setOnMouseEntered(e -> {this.nextPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #9AA6B2;-fx-background-radius: 20");});
            this.nextPage.setOnMouseExited(e -> {this.nextPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");});

            this.previousPage.setOnMouseEntered(e -> {this.previousPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #9AA6B2;-fx-background-radius: 20");});
            this.previousPage.setOnMouseExited(e -> {this.previousPage.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");});

            this.pageLabel.setStyle("-fx-font-size : 18; -fx-font-weight: bold");
            pageBox.setStyle("-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");
            
            pageBox.setMaxWidth(ApplicationSAE.width * 0.4);
            HBox.setMargin(pageBox, new Insets(20,0,5,0));
        }

        BorderPane.setAlignment(pageBox, Pos.CENTER);
        VBox.setMargin(pageBox, new Insets(20,0,0,0));

        HBox bottom = new HBox(20, this.commander, pageBox, this.home);
        HBox.setMargin(this.previousPage, new Insets(10, 10, 20, 10));
        HBox.setMargin(this.nextPage, new Insets(10, 10, 20, 10));
        HBox.setMargin(this.commander, new Insets(10, 60, 20, 10));
        HBox.setMargin(this.home, new Insets(10, 10, 20, 60));
        bottom.setAlignment(Pos.CENTER);

        this.setBottom(bottom);
    }

    private void initEmptyUI() {

        Text emptyText = new Text("Votre Panier est vide");
        emptyText.setFont(Font.font("arial", 25));
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
        this.pageLabel.setText(this.page+"");

        VBox center = new VBox(10);
        center.setAlignment(Pos.CENTER);

        Accordion accordion = new Accordion();
        boolean expanded = true;
        List<Integer> libs = new ArrayList<>(client.getPanier().getContenu().keySet());

        for (int i = (this.page - 1) * 5; i < maxPage * 5; i++) {

            if (i < libs.size()) {
                Integer libID = libs.get(i);
                Map<Livre, Integer> livres = client.getPanier().getContenu().get(libID);

                try {
                    TitledPane pane = new LibPanierPanel(libID, this.app,this, livres);
                    if (expanded) {
                        Platform.runLater(() -> pane.setExpanded(true));
                        expanded = false;
                    }
                    accordion.getPanes().add(pane);
                } catch (LibraryNotFoundException e) {
                }
            }
        }

        accordion.getPanes().get(0).setExpanded(true);
        accordion.setPrefHeight(ApplicationSAE.height * 0.8);
        accordion.setStyle("-fx-background-color: #e9e9e9F2; -fx-padding: 40; -fx-spacing: 10;");

        this.prixTot.setText("Prix total du panier : " + this.app.getClient().getPanier().getPrixTotal() + " €");
        this.prixTot.setStyle("-fx-font-size: 20");
        this.nbElementPanier.setText(this.app.getClient().getPanier().getNbElements() + " Livres dans la commande");
        this.nbElementPanier.setStyle("-fx-font-size: 17");

        VBox infoPanier = new VBox(10, this.nbElementPanier, this.prixTot);
        VBox.setMargin(this.nbElementPanier, new Insets(10, 10, 40, 10));
        VBox.setMargin(this.prixTot, new Insets(10, 10, 40, 10));
        infoPanier.setAlignment(Pos.CENTER_RIGHT);

        VBox centerBox = new VBox(10, accordion, infoPanier);
        this.setCenter(centerBox);

        // bottom

        this.commander.setDisable(false);
        this.previousPage.setDisable(false);
        this.nextPage.setDisable(false);

        HBox pageBox = new HBox(10);

        this.pageLabel = new Label(this.page + "");
        this.pageLabel.setStyle("-fx-font-size:20;-fx-font-weight:bold");

        pageBox.getChildren().addAll(this.previousPage, pageLabel, this.nextPage);
        pageBox.setAlignment(Pos.CENTER);

        if (ApplicationSAE.lightMode) {
            this.nextPage.setStyle(
                    "-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");
            this.previousPage.setStyle(
                    "-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");

            this.nextPage.setOnMouseEntered(e -> {
                this.nextPage.setStyle(
                        "-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #9AA6B2;-fx-background-radius: 20");
            });
            this.nextPage.setOnMouseExited(e -> {
                this.nextPage.setStyle(
                        "-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");
            });

            this.previousPage.setOnMouseEntered(e -> {
                this.previousPage.setStyle(
                        "-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #9AA6B2;-fx-background-radius: 20");
            });
            this.previousPage.setOnMouseExited(e -> {
                this.previousPage.setStyle(
                        "-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");
            });

            this.pageLabel.setStyle("-fx-font-size : 18; -fx-font-weight: bold");
            pageBox.setStyle("-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");

            pageBox.setMaxWidth(ApplicationSAE.width * 0.4);
            HBox.setMargin(pageBox, new Insets(20, 0, 5, 0));
        }

        BorderPane.setAlignment(pageBox, Pos.CENTER);
        VBox.setMargin(pageBox, new Insets(20, 0, 0, 0));

        HBox bottom = new HBox(20, this.commander, pageBox, this.home);
        HBox.setMargin(this.previousPage, new Insets(10, 10, 20, 10));
        HBox.setMargin(this.nextPage, new Insets(10, 10, 20, 10));
        HBox.setMargin(this.commander, new Insets(10, 60, 20, 10));
        HBox.setMargin(this.home, new Insets(10, 10, 20, 60));
        bottom.setAlignment(Pos.CENTER);

        this.setBottom(bottom);
    }

    public void IncPage() {
        if(this.page < this.maxPage - 1) {
            this.page++;
        }
    }

    public void RedcPage() {
        if (this.page > 1) {
            this.page--;
        }
    }

    public void setCommandeMenu(){
        this.app.getStage().setScene(new Scene(new CommandeWindow(this.app)));
    }

    public void setPanierMenu(){
        this.app.getStage().setScene(new Scene(this));
    }
}
