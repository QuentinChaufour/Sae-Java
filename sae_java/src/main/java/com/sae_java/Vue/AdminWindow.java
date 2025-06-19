package com.sae_java.Vue;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import com.sae_java.Vue.controleur.*;
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Reseau;

public class AdminWindow extends BorderPane{
    private final ApplicationSAE app;
    private String couleur;
    private String couleurText;
    private HBox centre;

    public AdminWindow(ApplicationSAE app){
        this.app = app;
        this.centre = new HBox();
        this.minHeightProperty().set(ApplicationSAE.height);
        this.minWidthProperty().set(ApplicationSAE.width);

        BorderPane top = new BorderPane();
        Text titre = new Text("Tableau de bord : Livre Express");
        titre.setFont(Font.font("Arial", FontWeight.NORMAL, 50));
        Button boutonDeconnexion = new Button("Deconnexion");
        boutonDeconnexion.setAlignment(Pos.CENTER_LEFT);
        ImageView imageMaison = new ImageView(new Image("file:./src/main/resources/images/deconnexion_32px.png")); 
        boutonDeconnexion.setGraphic(imageMaison);
        boutonDeconnexion.setOnAction(new ControleurDeconnexion(app));
        Button boutonGraphique = new Button("Graphiques");
        ImageView imageGraphique = new ImageView(new Image("file:./src/main/resources/images/graphique.png"));
        BorderPane.setMargin(top, new Insets(0, 0, 10, 0));
        boutonGraphique.setOnAction(new ControleurGraphiqueWindow(this.app));
        boutonGraphique.setGraphic(imageGraphique);
        BorderPane.setMargin(boutonDeconnexion, new Insets(5));
        BorderPane.setAlignment(boutonDeconnexion, Pos.CENTER_LEFT);
        BorderPane.setAlignment(titre, Pos.CENTER);
        BorderPane.setMargin(boutonGraphique, new Insets(5));
        BorderPane.setAlignment(imageGraphique, Pos.CENTER_RIGHT);
        top.setLeft(boutonDeconnexion);
        top.setCenter(titre);
        top.setRight(boutonGraphique);
        this.setTop(top);
        VBox actions = new VBox();

        actions.setSpacing(20);
        actions.setPadding(new Insets(20));
        Text actionsReseau = new Text("Actions sur le réseau :");
        actionsReseau.setFont(Font.font("Arial", FontWeight.NORMAL, 32));
        Button ajouteLibrairie = new Button("Ajouter Librairie");
        ajouteLibrairie.setPrefWidth(200);
        ajouteLibrairie.setPrefHeight(40);
        ajouteLibrairie.setOnAction(new ControleurAffichageAjouterLib(this));
        Button retirerLibrairie = new Button("Retirer Librairie");
        retirerLibrairie.setPrefWidth(200);
        retirerLibrairie.setPrefHeight(40);
        retirerLibrairie.setOnAction(new ControleurAffichageRetirerLib(this));
        Button transfererLiv = new Button("Transferer Livre");
        transfererLiv.setPrefWidth(200);
        transfererLiv.setPrefHeight(40);
        transfererLiv.setOnAction(new ControleurAffichageTransfererLivre(this));
        Button consulterLivres = new Button("Consulter les livres");
        consulterLivres.setPrefWidth(200);
        consulterLivres.setPrefHeight(40);
        consulterLivres.setOnAction(new ControleurAffichageConsulterLivres(this));
        Button ajouterLivre = new Button("Ajouter un livre");
        ajouterLivre.setPrefWidth(200);
        ajouterLivre.setPrefHeight(40);
        ajouterLivre.setOnAction(new ControleurAffichageAjouterLivre(this));
        actions.getChildren().addAll(actionsReseau, ajouteLibrairie, retirerLibrairie, transfererLiv, consulterLivres, ajouterLivre);
        this.centre.getChildren().add(actions);
        this.setCenter(this.centre);
    }

    public VBox affichageAjouterLib(){
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20, 0, 0, 20)); // Ajoute une marge à gauche
        Text texteAjouterLib = new Text("Ajouter une nouvelle libairie à la base donnée :");
        Label labelNomLib = new Label("Nom :");
        Label labelVilleLib = new Label("Ville :");
        TextField nomLib = new TextField();
        TextField villeLib = new TextField();
        Button ajouteLibrairie = new Button("Ajouter Librairie");
        texteAjouterLib.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        vbox.getChildren().addAll(texteAjouterLib, labelNomLib, nomLib, labelVilleLib, villeLib, ajouteLibrairie);
        ajouteLibrairie.setOnAction(new ControleurAjouterLib(this, nomLib, villeLib));
        return vbox;
    }

    public VBox affichageRetirerLib(){
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20, 0, 0, 20)); // Ajoute une marge à gauche
        Text texteRetirerLib = new Text("Retirer une nouvelle libairie à la base donnée :");
        Label labelNomLib = new Label("Librairie :");
        ObservableList<Librairie> libListe = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> choiceBoxRetirerLib = new ChoiceBox<>();
        choiceBoxRetirerLib.setItems(libListe);
        choiceBoxRetirerLib.setValue(libListe.get(0));
        Button retirerLibrairie = new Button("Retirer Librairie");
        texteRetirerLib.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        vbox.getChildren().addAll(texteRetirerLib, labelNomLib, choiceBoxRetirerLib, retirerLibrairie);
        retirerLibrairie.setOnAction(new ControleurRetirerLib(this, choiceBoxRetirerLib));
        return vbox;
    }

    public VBox affichageTransfererLivre(){
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20, 0, 0, 20)); // Ajoute une marge à gauche
        Text texteTransfererLivre = new Text("Transférer un livre d'un librairie à une autre :");
        texteTransfererLivre.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        Label labelIsbn = new Label("ISBN :");
        TextField nomIsbn = new TextField();
        Label labelIdLibSrc = new Label("Libairire Source :");

        ObservableList<Librairie> libListeSrc = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> choiceBoxSrcLib = new ChoiceBox<>();
        choiceBoxSrcLib.setItems(libListeSrc);
        choiceBoxSrcLib.setValue(libListeSrc.get(0));

        Label labelIdLibTgt = new Label("Librairie Cible :");
        ObservableList<Librairie> libListeTgt = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> choiceBoxTgtLib = new ChoiceBox<>();
        choiceBoxTgtLib.setItems(libListeTgt); 
        choiceBoxTgtLib.setValue(libListeTgt.get(1));
        
        Label labelQte = new Label("Quantité :");
        TextField qteLiv = new TextField();

        Button transfererLiv = new Button("Transferer Livre");
        vbox.getChildren().addAll(texteTransfererLivre, labelIsbn, nomIsbn, labelIdLibSrc, choiceBoxSrcLib, labelIdLibTgt, choiceBoxTgtLib, labelQte, qteLiv, transfererLiv);
        transfererLiv.setOnAction(new ControleurTransfererLiv(nomIsbn, choiceBoxSrcLib, choiceBoxTgtLib, qteLiv));
        return vbox;
    }

    public VBox affichageConsulterLivres(){
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20, 0, 0, 20)); // Ajoute une marge à gauche
        Text texte = new Text("Consulter les livres de la librairie :");
        ObservableList<Librairie> libList = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> choiceBoxLibrairieAdmin = new ChoiceBox<>();
        choiceBoxLibrairieAdmin.setItems(libList);
        choiceBoxLibrairieAdmin.setValue(libList.get(0));
        Button consulterLivres = new Button("Consulter les livres");
        texte.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        consulterLivres.setOnAction(new ControleurConsultationLivres(app, choiceBoxLibrairieAdmin));
        vbox.getChildren().addAll(texte, choiceBoxLibrairieAdmin, consulterLivres);
        return vbox;
    }

    public VBox affichageAjouterLivre(){
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20, 0, 0, 20)); // Ajoute une marge à gauche
        Text ajouterLivText = new Text("Ajouter un livre dans la librairie :");

        ObservableList<Librairie> libListAjouterLiv = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> choiceBoxAjouterLivLibrairieAdmin = new ChoiceBox<>();
        choiceBoxAjouterLivLibrairieAdmin.setItems(libListAjouterLiv);
        choiceBoxAjouterLivLibrairieAdmin.setValue(libListAjouterLiv.get(0));

        Label labelISBN = new Label("ISBN :");
        TextField tfIsbn = new TextField();
        
        Label labelTitre = new Label("Titre :");
        TextField tfTtitre = new TextField();
        
        Label labelEditeur = new Label("Editeur :");
        TextField tfEditeur = new TextField();

        Label labelDatePubli = new Label("Date Publication :");
        TextField tfDatePubli = new TextField();
        
        Label labelPrix = new Label("Prix :");
        TextField tfPrix = new TextField();
        
        Label labelNbPages = new Label("Nombre de pages :");
        TextField tfNbPages = new TextField();

        Label labelClassification = new Label("Classification :");
        TextField tfClassification = new TextField();

        Label labelQuantite = new Label("Quantité :");
        TextField tfQuantite = new TextField();
        Button ajouterLivre = new Button("Ajouter un livre");
        ajouterLivText.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        vbox.getChildren().addAll(ajouterLivText, choiceBoxAjouterLivLibrairieAdmin, labelISBN, tfIsbn, labelTitre, tfTtitre, labelEditeur, tfEditeur, labelDatePubli, tfDatePubli, labelPrix, tfPrix, labelNbPages, tfNbPages, labelClassification, tfClassification, labelQuantite, tfQuantite, ajouterLivre);
        ajouterLivre.setOnAction(new ControleurAjouterLivreAdmin(this, choiceBoxAjouterLivLibrairieAdmin, tfIsbn, tfTtitre, tfEditeur, tfDatePubli, tfPrix, tfNbPages, tfClassification, tfQuantite));
        return vbox;
    }

    public VBox majAffichage(){
        VBox actions = new VBox();
        actions.setSpacing(50);
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
        ajouteLibrairie.setOnAction(new ControleurAjouterLib(this, nomLib, villeLib));

        // Retirer une librairie à la base

        HBox retirerLib = new HBox();
        retirerLib.setAlignment(Pos.CENTER_LEFT);
        retirerLib.setSpacing(10);
        actions.getChildren().add(retirerLib);
        Text texteRetirerLib = new Text("Retirer une nouvelle libairie à la base donnée :");
        Label labelLib = new Label("Librairie :");
        ObservableList<Librairie> libListe = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> choiceBoxRetirerLib = new ChoiceBox<>();
        choiceBoxRetirerLib.setItems(libListe);
        choiceBoxRetirerLib.setValue(libListe.get(0));
        texteRetirerLib.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        Button retirerLibrairie = new Button("Retirer Librairie");
        retirerLib.getChildren().addAll(texteRetirerLib, labelLib, choiceBoxRetirerLib, retirerLibrairie);
        retirerLibrairie.setOnAction(new ControleurRetirerLib(this, choiceBoxRetirerLib));

        // Transférer un livre dans la base
    
        HBox transfererLivre = new HBox();
        transfererLivre.setAlignment(Pos.CENTER_LEFT);
        transfererLivre.setSpacing(10);
        actions.getChildren().add(transfererLivre);
        Text texteTransferLivre = new Text("Transférer un livre d'un librairie à une autre :");

        Label labelIsbn = new Label("ISBN :");
        TextField ISBN = new TextField();

        Label labelIdLibSrc = new Label("Libairire Source :");
        ObservableList<Librairie> libListeSrc = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> choiceBoxSrcLib = new ChoiceBox<>();
        choiceBoxSrcLib.setItems(libListeSrc);
        choiceBoxSrcLib.setValue(libListeSrc.get(0));

        Label labelIdLibTgt = new Label("Librairie Cible :");
        ObservableList<Librairie> libListeTgt = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> choiceBoxTgtLib = new ChoiceBox<>();
        choiceBoxTgtLib.setItems(libListeTgt); 
        choiceBoxTgtLib.setValue(libListeTgt.get(1));
        
        Label labelQte = new Label("Quantité :");
        TextField qteLiv = new TextField();

        texteTransferLivre.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        Button transfererLiv = new Button("Transferer Livre");
        transfererLivre.getChildren().addAll(texteTransferLivre, labelIsbn, ISBN, labelIdLibSrc, choiceBoxSrcLib, labelIdLibTgt, choiceBoxTgtLib, labelQte, qteLiv, transfererLiv);
        transfererLiv.setOnAction(new ControleurTransfererLiv(ISBN, choiceBoxSrcLib, choiceBoxTgtLib, qteLiv));
        
        // Consulter les livres d'une librairie

        HBox consulterLiv = new HBox();
        consulterLiv.setAlignment(Pos.CENTER_LEFT);
        consulterLiv.setSpacing(10);
        actions.getChildren().add(consulterLiv);
        Text texte = new Text("Consulter les livres de la librairie :");
        texte.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        Button consulterLivres = new Button("Consulter les livres");
        ObservableList<Librairie> libList = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> choiceBoxLibrairieAdmin = new ChoiceBox<>();
        choiceBoxLibrairieAdmin.setItems(libList);
        choiceBoxLibrairieAdmin.setValue(libList.get(0));
        consulterLivres.setOnAction(new ControleurConsultationLivres(app, choiceBoxLibrairieAdmin));
        consulterLiv.getChildren().addAll(texte, choiceBoxLibrairieAdmin, consulterLivres);

        // Ajouter un livre à une librairie
    
        HBox ajouterLiv = new HBox();
        actions.getChildren().add(ajouterLiv);
        ajouterLiv.setAlignment(Pos.CENTER_LEFT);
        ajouterLiv.setSpacing(10);
        Text ajouterLivText = new Text("Ajouter un livre dans la librairie :");
        ajouterLivText.setFont(Font.font("Arial", FontWeight.NORMAL, 24));

        Button ajouterLivre = new Button("Ajouter un livre");

        ObservableList<Librairie> libListAjouterLiv = FXCollections.observableList(Reseau.librairies);
        ChoiceBox<Librairie> choiceBoxAjouterLivLibrairieAdmin = new ChoiceBox<>();
        choiceBoxAjouterLivLibrairieAdmin.setItems(libListAjouterLiv);
        choiceBoxAjouterLivLibrairieAdmin.setValue(libListAjouterLiv.get(0));

        Label labelISBN = new Label("ISBN :");
        TextField tfIsbn = new TextField();
        
        Label labelTitre = new Label("Titre :");
        TextField tfTtitre = new TextField();
        
        Label labelEditeur = new Label("Editeur :");
        TextField tfEditeur = new TextField();

        Label labelDatePubli = new Label("Date Publication :");
        TextField tfDatePubli = new TextField();
        
        Label labelPrix = new Label("Prix :");
        TextField tfPrix = new TextField();
        
        Label labelNbPages = new Label("Nombre de pages :");
        TextField tfNbPages = new TextField();

        Label labelClassification = new Label("Classification :");
        TextField tfClassification = new TextField();

        Label labelQuantite = new Label("Quantité :");
        TextField tfQuantite = new TextField();
        HBox ajouterLivl2 = new HBox();
        ajouterLivl2.setSpacing(10);
        ajouterLivl2.setAlignment(Pos.CENTER);
        actions.getChildren().add(ajouterLivl2);
        ajouterLivre.setOnAction(new ControleurAjouterLivreAdmin(this, choiceBoxAjouterLivLibrairieAdmin, tfIsbn, tfTtitre, tfEditeur, tfDatePubli, tfPrix, tfNbPages, tfClassification, tfQuantite));
        ajouterLiv.getChildren().addAll(ajouterLivText, choiceBoxAjouterLivLibrairieAdmin, labelISBN, tfIsbn, labelTitre, tfTtitre, labelEditeur, tfEditeur, labelDatePubli, tfDatePubli);
        ajouterLivl2.getChildren().addAll(labelPrix, tfPrix, labelNbPages, tfNbPages, labelClassification, tfClassification, labelQuantite, tfQuantite, ajouterLivre);

        return actions;
    }

    public HBox getCentre(){
        return this.centre;
    }
}
