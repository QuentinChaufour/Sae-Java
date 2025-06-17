package com.sae_java.Vue;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import com.sae_java.Vue.controleur.*;

import com.sae_java.Modele.Client;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;

public class AdminWindow extends BorderPane{
    private final ApplicationSAE app;

    public AdminWindow(ApplicationSAE app){
        this.app = app;

        this.minHeightProperty().set(900);
        this.minWidthProperty().set(1800);

        BorderPane top = new BorderPane();
        Text texte = new Text("Tableau de bord : Livre Express");
        texte.setFont(Font.font("Arial", FontWeight.NORMAL, 50));
        Button boutonDeconnexion = new Button("Deconnexion");
        boutonDeconnexion.setAlignment(Pos.CENTER_LEFT);
        ImageView imageMaison = new ImageView(new Image("file:./src/main/resources/images/deconnexion_32px.png")); 
        boutonDeconnexion.setGraphic(imageMaison);
        boutonDeconnexion.setOnAction(new ControleurDeconnexion(app));
        BorderPane.setMargin(top, new Insets(0, 0, 10, 0));
        BorderPane.setMargin(boutonDeconnexion, new Insets(5));
        BorderPane.setAlignment(boutonDeconnexion, Pos.CENTER_LEFT);
        BorderPane.setAlignment(texte, Pos.CENTER);
        top.setLeft(boutonDeconnexion);
        top.setCenter(texte);
        this.setTop(top);

        HBox utilisateur = new HBox();
        Text nomClient = new Text("Dupont Jean");
        nomClient.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        ImageView imageUtilisateur = new ImageView(new Image("file:./src/main/resources/images/utilisateur_32px.png"));
        utilisateur.getChildren().addAll(imageUtilisateur, nomClient);
        this.setLeft(utilisateur);

        GridPane actions = new GridPane();
        actions.setVgap(10);
        actions.setHgap(5);
        actions.setPadding(new Insets(20));
        Text actionsReseau = new Text("Actions  sur le réseau:");
        actionsReseau.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
        actions.add(actionsReseau, 0, 0);

        Text texteAjouterLib = new Text("Ajouter une nouvelle libairie à la base donnée : ");
        Label labelNomLib = new Label("Nom : ");
        Label labelvilleLib = new Label("Ville : ");
        TextField nomLib = new TextField();
        TextField villeLib = new TextField();
        texteAjouterLib.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        actions.add(texteAjouterLib, 0, 1);
        Button ajouteLibrairie = new Button("Ajouter Librairie");
        actions.add(labelNomLib, 1, 1);
        actions.add(nomLib, 2, 1);
        actions.add(labelvilleLib, 3, 1);
        actions.add(villeLib, 4, 1);
        actions.add(ajouteLibrairie, 5, 1);
        ajouteLibrairie.setOnAction(new ControleurAjouterLib(app, nomLib, villeLib));
        
        Text texteRetirerLib = new Text("Retirer une nouvelle libairie à la base donnée : ");
        Label labelId = new Label("ID : ");
        TextField IdLib = new TextField();
        texteAjouterLib.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        actions.add(texteRetirerLib, 0, 2);
        Button retirerLibrairie = new Button("Retirer Librairie");
        actions.add(labelId, 1, 2);
        actions.add(IdLib, 2, 2);
        actions.add(retirerLibrairie, 3, 2);
        retirerLibrairie.setOnAction(new ControleurRetirerLib(app, IdLib));

        this.setCenter(actions);
    }


}
