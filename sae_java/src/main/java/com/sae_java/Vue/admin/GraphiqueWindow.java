package com.sae_java.Vue.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sae_java.Modele.Auteur;
import com.sae_java.Modele.Enumerations.EnumPalmares;
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.controleur.ControleurCaByLibrairie;
import com.sae_java.Vue.controleur.ControleurPalmaresAuteur;
import com.sae_java.Vue.controleur.ControleurPalmaresLibrairie;
import com.sae_java.Vue.controleur.ControleurPalmaresLivre;
import com.sae_java.Vue.controleur.ControleurRetourAdmin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GraphiqueWindow extends BorderPane{

    private final ApplicationSAE app;
    private List<String> types;

    public GraphiqueWindow(ApplicationSAE app){
        this.app = app;
        this.types = new ArrayList<>();
        this.types.add("CAMEMBERT");
        this.types.add("BATON");
        this.minHeightProperty().set(ApplicationSAE.height);
        this.minWidthProperty().set(ApplicationSAE.width);

        BorderPane top = new BorderPane();
        Text titre = new Text("Consultation des statistiques");
        titre.setFont(Font.font("Arial", FontWeight.NORMAL, 50));
        Button boutonDeconnexion = new Button("Retour");
        boutonDeconnexion.setAlignment(Pos.CENTER_LEFT);
        boutonDeconnexion.setOnAction(new ControleurRetourAdmin(app));
        if (ApplicationSAE.lightMode) {

            boutonDeconnexion.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #BCCCDC;-fx-background-radius: 20");

            boutonDeconnexion.setOnMouseExited(e -> {boutonDeconnexion.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #BCCCDC;-fx-background-radius: 20");});
            boutonDeconnexion.setOnMouseEntered(e -> {boutonDeconnexion.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color :#9AA6B2;-fx-background-radius: 20");});

            boutonDeconnexion.setPrefWidth(ApplicationSAE.width * 0.2);
        }

        BorderPane.setMargin(top, new Insets(0, 0, 10, 0));
        BorderPane.setMargin(boutonDeconnexion, new Insets(10,10,10,40));
        BorderPane.setAlignment(titre, Pos.CENTER);
        top.setLeft(boutonDeconnexion);
        top.setCenter(titre);
        this.setTop(top);
        VBox options = new VBox();
        options.setPadding(new Insets(10));
        options.setSpacing(10);

        ObservableList<String> typeListe = FXCollections.observableList(this.types);
        ChoiceBox<String> choiceBoxType = new ChoiceBox<>();
        choiceBoxType.setItems(typeListe);
        choiceBoxType.setValue(typeListe.get(0));

        Text titreGraphique = new Text("Graphiques :");
        Text titreType = new Text("Type de graphique :");

        Button boutonPalmaresLivre = new Button("Palmares des livres");
        Button boutonPalmaresLibrairie = new Button("Palmares des librairies");
        Button boutonPalmaresAuteur = new Button("Palmares des auteurs");
        Button boutonCAByLibrairie = new Button("CA des librairies  ");
        
        boutonPalmaresLivre.setOnAction(new ControleurPalmaresLivre(this, choiceBoxType));
        boutonPalmaresLibrairie.setOnAction(new ControleurPalmaresLibrairie(this, choiceBoxType));
        boutonPalmaresAuteur.setOnAction(new ControleurPalmaresAuteur(this, choiceBoxType));
        boutonCAByLibrairie.setOnAction(new ControleurCaByLibrairie(this, choiceBoxType));

        options.getChildren().addAll(titreGraphique, boutonPalmaresLivre, boutonPalmaresLibrairie, boutonPalmaresAuteur, boutonCAByLibrairie, titreType, choiceBoxType);
        options.setAlignment(Pos.CENTER);
        if(ApplicationSAE.lightMode){
            options.setStyle("-fx-margin: 15;-fx-padding: 15; -fx-background-color: #9AA6B2;-fx-background-radius: 0 20 0 0");
        }

        options.setPrefWidth(ApplicationSAE.width * 0.2);
        this.setLeft(options);

        boutonCAByLibrairie.fire();
    }   

    public VBox CAByLibrairieCambembert(){
        PieChart pieChart = new PieChart();
        VBox vbox = new VBox();
        Map<Librairie, Double> val = Reseau.CAByLibrairie();
	    pieChart.setTitle("CA par Magasin");
	    for (Map.Entry<Librairie, Double> entree: val.entrySet()){
            pieChart.getData().add(new PieChart.Data(entree.getKey().getNom(), entree.getValue()));
        }
        vbox.getChildren().addAll(pieChart);
        return vbox;
    }

    public VBox CAByLibrairieBaton(){
        Map<Librairie, Double> val = Reseau.CAByLibrairie();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Magasin");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("CA");
        // Création du graphique                                                
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("CA par Magasin");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Chiffre d'affaire");
        // Ajout des données de la map au graphique
        for (Map.Entry<Librairie, Double> entree : val.entrySet()) {
            series.getData().add(new XYChart.Data<>(entree.getKey().getNom(), entree.getValue()));
        }
        barChart.getData().add(series);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(barChart);
        return vbox;

    }

    public VBox PalmaresLivreCambembert(){
        PieChart pieChart = new PieChart();
        VBox vbox = new VBox();
        Map<Livre, Integer> mapPalmaresLivre;
        try {
            mapPalmaresLivre = Reseau.getPalmares(10, EnumPalmares.LIVRE);
            pieChart.setTitle("Palmares Livre");
	    for (Map.Entry<Livre, Integer> entree: mapPalmaresLivre.entrySet()){
            pieChart.getData().add(new PieChart.Data(entree.getKey().getTitre(), entree.getValue()));
        }
        vbox.getChildren().addAll(pieChart);
        return vbox;
        } catch (SQLException e) {
            System.err.println("erreur lors de la connection SQL");
            e.printStackTrace();
        }
        Text texte = new Text("Erreur lors du chargement du graphique");
        vbox.getChildren().add(texte);
        return vbox;
    }

    public VBox PalmaresLivreBaton(){
        Map<Livre, Integer> mapPalmaresLivre;
        VBox vbox = new VBox();
        try {
            mapPalmaresLivre = Reseau.getPalmares(10, EnumPalmares.LIVRE);
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Livre");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Score");
            // Création du graphique                                                
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Palmares Livre");
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Score");
            // Ajout des données de la map au graphique
            for (Map.Entry<Livre, Integer> entree : mapPalmaresLivre.entrySet()) {
                series.getData().add(new XYChart.Data<>(entree.getKey().getTitre(), entree.getValue()));
            }
            barChart.getData().add(series);
            vbox.getChildren().addAll(barChart);
            return vbox;

        } catch (SQLException e) {
            System.err.println("Erreur de connexion SQL");
            e.printStackTrace();
        }
        Text texte = new Text("Erreur lors du chargement du graphique");
        vbox.getChildren().add(texte);
        return vbox;


    }

    public VBox PalmaresLibrairieCambembert(){
        PieChart pieChart = new PieChart();
        VBox vbox = new VBox();
        Map<Librairie, Integer> mapPalmaresLibrairie;
        try {
            mapPalmaresLibrairie = Reseau.getPalmares(10, EnumPalmares.LIBRAIRIE);
            pieChart.setTitle("Palmares Livre");
	    for (Map.Entry<Librairie, Integer> entree: mapPalmaresLibrairie.entrySet()){
            pieChart.getData().add(new PieChart.Data(entree.getKey().getNom(), entree.getValue()));
        }
        vbox.getChildren().addAll(pieChart);
        return vbox;
        } catch (SQLException e) {
            System.err.println("erreur lors de la connection SQL");
            e.printStackTrace();
        }
        Text texte = new Text("Erreur lors du chargement du graphique");
        vbox.getChildren().add(texte);
        return vbox;
    }

    public VBox PalmaresLibrairieBaton(){
        Map<Librairie, Integer> mapPalmaresLibrairie;
        VBox vbox = new VBox();
        try {
            mapPalmaresLibrairie = Reseau.getPalmares(10, EnumPalmares.LIBRAIRIE);
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Livre");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Score");
            // Création du graphique                                                
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Palmares Livre");
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Score");
            // Ajout des données de la map au graphique
            for (Map.Entry<Librairie, Integer> entree : mapPalmaresLibrairie.entrySet()) {
                series.getData().add(new XYChart.Data<>(entree.getKey().getNom(), entree.getValue()));
            }
            barChart.getData().add(series);
            vbox.getChildren().addAll(barChart);
            return vbox;

        } catch (SQLException e) {
            System.err.println("Erreur de connexion SQL");
            e.printStackTrace();
        }
        Text texte = new Text("Erreur lors du chargement du graphique");
        vbox.getChildren().add(texte);
        return vbox;


    }

    public VBox PalmaresAuteurCambembert(){
        PieChart pieChart = new PieChart();
        VBox vbox = new VBox();
        Map<Auteur, Integer> mapPalmaresAuteur;
        try {
            mapPalmaresAuteur = Reseau.getPalmares(10, EnumPalmares.AUTEUR);
            pieChart.setTitle("Palmares Livre");
	    for (Map.Entry<Auteur, Integer> entree: mapPalmaresAuteur.entrySet()){
            pieChart.getData().add(new PieChart.Data(entree.getKey().getNomPrenom(), entree.getValue()));
        }
        vbox.getChildren().addAll(pieChart);
        return vbox;
        } catch (SQLException e) {
            System.err.println("erreur lors de la connection SQL");
            e.printStackTrace();
        }
        Text texte = new Text("Erreur lors du chargement du graphique");
        vbox.getChildren().add(texte);
        return vbox;
    }

    public VBox PalmaresAuteurBaton(){
        Map<Auteur, Integer> mapPalmaresAuteur;
        VBox vbox = new VBox();
        try {
            mapPalmaresAuteur = Reseau.getPalmares(10, EnumPalmares.AUTEUR);
            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Livre");
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Score");
            // Création du graphique                                                
            BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
            barChart.setTitle("Palmares Livre");
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Score");
            // Ajout des données de la map au graphique
            for (Map.Entry<Auteur, Integer> entree : mapPalmaresAuteur.entrySet()) {
                series.getData().add(new XYChart.Data<>(entree.getKey().getNomPrenom(), entree.getValue()));
            }
            barChart.getData().add(series);
            vbox.getChildren().addAll(barChart);
            return vbox;

        } catch (SQLException e) {
            System.err.println("Erreur de connexion SQL");
            e.printStackTrace();
        }
        Text texte = new Text("Erreur lors du chargement du graphique");
        vbox.getChildren().add(texte);
        return vbox;


    }
}

    

