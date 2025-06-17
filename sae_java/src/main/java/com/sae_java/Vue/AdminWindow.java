package com.sae_java.Vue;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Exceptions.BookNotInStockException;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;

public class AdminWindow extends BorderPane{
    private final ApplicationSAE app;

    public AdminWindow(ApplicationSAE app){
        this.app = app;

        this.minHeightProperty().set(900);
        this.minWidthProperty().set(1800);

        BorderPane top = new BorderPane();
        Text titre = new Text("Tableau de bord : Livre Express");
        titre.setFont(Font.font("Arial", FontWeight.NORMAL, 50));
        Button boutonDeconnexion = new Button("Deconnexion");
        boutonDeconnexion.setAlignment(Pos.CENTER_LEFT);
        ImageView imageMaison = new ImageView(new Image("file:./src/main/resources/images/deconnexion_32px.png")); 
        boutonDeconnexion.setGraphic(imageMaison);
        boutonDeconnexion.setOnAction(new ControleurDeconnexion(app));
        BorderPane.setMargin(top, new Insets(0, 0, 10, 0));
        BorderPane.setMargin(boutonDeconnexion, new Insets(5));
        BorderPane.setAlignment(boutonDeconnexion, Pos.CENTER_LEFT);
        BorderPane.setAlignment(titre, Pos.CENTER);
        top.setLeft(boutonDeconnexion);
        top.setCenter(titre);
        this.setTop(top);

        VBox actions = new VBox();
        actions.setSpacing(10);
        HBox titreActions = new HBox();
        titreActions.setAlignment(Pos.CENTER_LEFT);
        titreActions.setSpacing(10);
        actions.getChildren().add(titreActions);
        actions.setPadding(new Insets(20));
        Text actionsReseau = new Text("Actions sur le réseau :");
        actionsReseau.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
        titreActions.getChildren().add(actionsReseau);

        // Ajouter une nouvelle librairie à la base

        HBox ajouterLib = new HBox();
        ajouterLib.setAlignment(Pos.CENTER_LEFT);
        ajouterLib.setSpacing(10);
        actions.getChildren().add(ajouterLib);
        Text texteAjouterLib = new Text("Ajouter une nouvelle libairie à la base donnée :");
        Label labelNomLib = new Label("Nom :");
        Label labelVilleLib = new Label("Ville :");
        TextField nomLib = new TextField();
        TextField villeLib = new TextField();
        texteAjouterLib.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
                Button ajouteLibrairie = new Button("Ajouter Librairie");
        ajouterLib.getChildren().addAll(texteAjouterLib, labelNomLib, nomLib, labelVilleLib, villeLib, ajouteLibrairie);
        ajouteLibrairie.setOnAction(new ControleurAjouterLib(app, nomLib, villeLib));

        // Retirer une librairie à la base

        HBox retirerLib = new HBox();
        retirerLib.setAlignment(Pos.CENTER_LEFT);
        retirerLib.setSpacing(10);
        actions.getChildren().add(retirerLib);
        Text texteRetirerLib = new Text("Retirer une nouvelle libairie à la base donnée :");
        Label labelId = new Label("ID :");
        TextField IdLib = new TextField();
        texteRetirerLib.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        Button retirerLibrairie = new Button("Retirer Librairie");
        retirerLib.getChildren().addAll(texteRetirerLib, labelId, IdLib, retirerLibrairie);
        retirerLibrairie.setOnAction(new ControleurRetirerLib(app, IdLib));

        // Transférer un livre dans la base

        HBox transfererLivre = new HBox();
        transfererLivre.setAlignment(Pos.CENTER_LEFT);
        transfererLivre.setSpacing(10);
        actions.getChildren().add(transfererLivre);
        Text texteTransferLivre = new Text("Ajouter une nouvelle libairie à la base donnée :");

        Label labelIsbn = new Label("ISBN :");
        TextField ISBN = new TextField();

        Label labelIdLibSrc = new Label("ID Libairire Source :");
        TextField IdLibSrc = new TextField();

        Label labelIdLibTgt = new Label("ID Librairie Cible :");
        TextField IdLibTgt = new TextField();
        
        Label labelQte = new Label("Quantité :");
        TextField qteLiv = new TextField();

        texteTransferLivre.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        Button transfererLiv = new Button("Transferer Livre");
        transfererLivre.getChildren().addAll(texteTransferLivre, labelIsbn, ISBN, labelIdLibSrc, IdLibSrc, labelIdLibTgt, IdLibTgt, labelQte, qteLiv, transfererLiv);
        transfererLiv.setOnAction(new ControleurTransfererLiv(app, ISBN, IdLibSrc, IdLibTgt, qteLiv));
        
        // Consulter les livres d'une librairie

        HBox consulterLiv = new HBox();
        consulterLiv.setAlignment(Pos.CENTER_LEFT);
        consulterLiv.setSpacing(10);
        actions.getChildren().add(consulterLiv);
        Text texte = new Text("Consulter les livres de la librairie :");
        texte.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        ObservableList<Librairie> libList = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> choiceBoxLibrairieAdmin = new ChoiceBox<>();
        Button consulterLivres = new Button("Consulter les livres");
        consulterLivres.setOnAction(new ControleurConsultationLivres(app, choiceBoxLibrairieAdmin));
        
        choiceBoxLibrairieAdmin.setItems(libList);
        choiceBoxLibrairieAdmin.setValue(libList.get(0));
        consulterLiv.getChildren().addAll(texte, choiceBoxLibrairieAdmin, consulterLivres);
        // actions.add(texte, 0, 4);
        // actions.add(choiceBoxLibrairieAdmin, 1, 4);
        
        this.setCenter(actions);
    }


}
