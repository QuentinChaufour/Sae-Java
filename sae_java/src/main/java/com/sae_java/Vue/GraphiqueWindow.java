package com.sae_java.Vue;

import java.util.Map;

import javax.swing.ButtonGroup;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Vue.controleur.ControleurAjouterLivre;
import com.sae_java.Vue.controleur.ControleurAjouterLivreTab;
import com.sae_java.Vue.controleur.ControleurDeconnexion;
import com.sae_java.Vue.controleur.ControleurRetirerLivre;
import com.sae_java.Vue.controleur.ControleurRetourAdmin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class GraphiqueWindow extends BorderPane{

    private final ApplicationSAE app;

    public GraphiqueWindow(ApplicationSAE app){
        this.app = app;

        this.minHeightProperty().set(900);
        this.minWidthProperty().set(1800);

        BorderPane top = new BorderPane();
        Text titre = new Text("Consultation des livres de la librairie : ");
        titre.setFont(Font.font("Arial", FontWeight.NORMAL, 50));
        Button boutonDeconnexion = new Button("Retour");
        boutonDeconnexion.setAlignment(Pos.CENTER_LEFT);
        boutonDeconnexion.setOnAction(new ControleurRetourAdmin(app));
        BorderPane.setMargin(top, new Insets(0, 0, 10, 0));
        BorderPane.setMargin(boutonDeconnexion, new Insets(5));
        BorderPane.setAlignment(boutonDeconnexion, Pos.CENTER_LEFT);
        BorderPane.setAlignment(titre, Pos.CENTER);
        top.setLeft(boutonDeconnexion);
        top.setCenter(titre);
        this.setTop(top);

        this.setCenter(CAByLibrairie(Reseau.CAByLibrairie()));
        

        
    }   

    public VBox CAByLibrairie(Map<Librairie,Double> mapCAByLibrairie){
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
        for (Map.Entry<Librairie, Double> entree : mapCAByLibrairie.entrySet()) {
            series.getData().add(new XYChart.Data<>(entree.getKey().getNom(), entree.getValue()));
        }
        barChart.getData().add(series);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(barChart);
        return vbox;

    }
}


    

