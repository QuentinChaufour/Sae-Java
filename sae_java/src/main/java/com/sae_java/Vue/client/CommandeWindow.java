package com.sae_java.Vue.client;

import java.util.Map;

import com.sae_java.Modele.Livre;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.controleur.ControleurCommander;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CommandeWindow extends BorderPane{
 
    private ApplicationSAE app;

    private RadioButton aDomicile;
    private RadioButton enLibrairie;
    private RadioButton enLibrairieC;
    private RadioButton enLigne;
    private RadioButton facture;
    private RadioButton noFacture;
    
    public CommandeWindow(ApplicationSAE app){
        this.app = app;
        this.setStyle("-fx-background-color : #F5F5F5");

        this.setPrefSize(ApplicationSAE.width, ApplicationSAE.height);

        TableView<Map.Entry<Livre, Integer>> tableau = this.initResume();
        tableau.setPrefWidth(ApplicationSAE.width *0.6);
        VBox choose = this.confCommande();

        HBox center = new HBox(10);
        center.getChildren().addAll(tableau,choose);

        HBox.setMargin(tableau, new Insets(20));
        HBox.setMargin(choose, new Insets(20));

        this.setCenter(center);
    }

    private TableView<Map.Entry<Livre, Integer>> initResume(){

        // implémentation des Entry<Livre,Integer> avec aide de l'IA

        TableView<Map.Entry<Livre, Integer>> table = new TableView<>();

        // Colonnes du tableau
        TableColumn<Map.Entry<Livre, Integer>, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().getTitre()));

        TableColumn<Map.Entry<Livre, Integer>, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().getIsbn()));

        TableColumn<Map.Entry<Livre, Integer>, Double> prixCol = new TableColumn<>("Prix");
        prixCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getKey().getPrix()).asObject());

        TableColumn<Map.Entry<Livre, Integer>, Integer> quantiteCol = new TableColumn<>("Quantité");
        quantiteCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValue()).asObject());

        table.getColumns().add(titreCol);
        table.getColumns().add(isbnCol);
        table.getColumns().add(prixCol);
        table.getColumns().add(quantiteCol);

        for(Map<Livre,Integer> livres : this.app.getClient().getPanier().getContenu().values()){
            ObservableList<Map.Entry<Livre, Integer>> items = FXCollections.observableArrayList(livres.entrySet());
            table.setItems(items);
        }

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        return table;
    }

    private VBox confCommande(){

        VBox option = new VBox(10);

        ToggleGroup radio = new ToggleGroup();

        this.aDomicile = new RadioButton("A domicile");
        this.aDomicile.setSelected(true);;
        this.enLibrairie = new RadioButton("En librairie");

        aDomicile.setToggleGroup(radio);
        enLibrairie.setToggleGroup(radio);

        HBox radioLivraison = new HBox(10,aDomicile,enLibrairie);
        radioLivraison.setAlignment(Pos.CENTER);
        TitledPane chooseLivraison = new TitledPane("Choix de livraison",radioLivraison);


        ToggleGroup radioEnLigneToggle = new ToggleGroup();

        this.enLibrairieC = new RadioButton("En librairie");
        this.enLigne = new RadioButton("En ligne");
        this.enLigne.setSelected(true);

        enLibrairieC.setToggleGroup(radioEnLigneToggle);
        enLigne.setToggleGroup(radioEnLigneToggle);

        HBox radioEnLigne = new HBox(10, enLibrairieC, enLigne);
        radioEnLigne.setAlignment(Pos.CENTER);
        TitledPane chooseEnLigne = new TitledPane("Type de Commande", radioEnLigne);
        chooseEnLigne.setExpanded(true);
        chooseEnLigne.setCollapsible(false);

        chooseLivraison.setExpanded(true);
        chooseLivraison.setCollapsible(false);

        ToggleGroup radioFactureGroup = new ToggleGroup();

        this.facture = new RadioButton("Faire facture");
        this.noFacture = new RadioButton("Sans facture");
        this.noFacture.setSelected(true);

        this.facture.setToggleGroup(radioFactureGroup);
        this.noFacture.setToggleGroup(radioFactureGroup);

        HBox radioFacture = new HBox(10, facture, noFacture);
        radioFacture.setAlignment(Pos.CENTER);
        TitledPane chooseFacture = new TitledPane("Choix de livraison", radioFacture);
        chooseFacture.setCollapsible(false);

        HBox btn = new HBox(10);
        Button confirm = new Button("Commander");
        confirm.setOnAction(new ControleurCommander(this.app, this));
        confirm.setStyle("-fx-font-size: 15");

        if(ApplicationSAE.lightMode){
            confirm.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");

            confirm.setOnMouseEntered(e -> {confirm.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #9AA6B2;-fx-background-radius: 20");});
            confirm.setOnMouseExited(e -> {confirm.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");});
        }

        Button stop = new Button("Annuler");
        stop.setOnAction((ActionEvent) -> {this.fermer();});

        if(ApplicationSAE.lightMode){
            stop.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");

            stop.setOnMouseEntered(e -> {stop.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #ffa5a5;-fx-background-radius: 20");});
            stop.setOnMouseExited(e -> {stop.setStyle("-fx-margin: 10 50 10 50;-fx-padding: 10;-fx-background-color : #BCCCDC;-fx-background-radius: 20");});
        }

        btn.getChildren().addAll(confirm,stop);

        Text prixTot = new Text("Total de la commande" + this.app.getClient().getPanier().getPrixTotal() + " €");
        Text nbElementPanier = new Text("Comprennant " + this.app.getClient().getPanier().getNbElements() + " Livres dans la commande");

        prixTot.setStyle("-fx-font-size: 18;-fx-font-weight: bold");
        nbElementPanier.setStyle("-fx-font-size: 18");

        VBox infoPanier = new VBox(10, nbElementPanier, prixTot);
        VBox.setMargin(nbElementPanier, new Insets(10, 20, 20, 10));
        VBox.setMargin(prixTot, new Insets(10, 20, 20, 10));
        infoPanier.setAlignment(Pos.CENTER_RIGHT);

        option.getChildren().addAll(chooseEnLigne, chooseLivraison, chooseFacture,infoPanier,btn);

        return option;
    }

    public boolean getFacture(){
        return this.facture.isSelected();
    }

    public String getLivraison(){
        if(this.enLibrairie.isSelected()){
            return "M";
        }
        else{
            return "C";
        }
    }

    public boolean getEnLigne(){
        return this.enLigne.isSelected();
    }

    public void fermer(){
        this.app.getStage().setScene(new Scene(new PanierClientWindow(this.app)));
    }
}
