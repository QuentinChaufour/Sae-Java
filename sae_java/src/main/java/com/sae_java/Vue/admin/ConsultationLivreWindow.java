package com.sae_java.Vue.admin;

import java.util.Map;

import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.controleur.ControleurAjouterLivreTab;
import com.sae_java.Vue.controleur.ControleurRetirerLivre;
import com.sae_java.Vue.controleur.ControleurRetourAdmin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class ConsultationLivreWindow extends BorderPane{

    private final ApplicationSAE app;
    private Librairie librairie;

    public ConsultationLivreWindow(ApplicationSAE app, Librairie librairie){
        this.app = app;
        this.librairie = librairie;

        this.minHeightProperty().set(ApplicationSAE.height);
        this.minWidthProperty().set(ApplicationSAE.width);

        BorderPane top = new BorderPane();
        Text titre = new Text("Consultation des livres de la librairie : " + this.librairie.getNom());
        titre.setFont(Font.font("Arial", 40));
        Button boutonDeconnexion = new Button("Retour");
        boutonDeconnexion.setOnAction(new ControleurRetourAdmin(app));

        if (ApplicationSAE.lightMode) {

            boutonDeconnexion.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #BCCCDC;-fx-background-radius: 20");

            boutonDeconnexion.setOnMouseExited(e -> {boutonDeconnexion.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #BCCCDC;-fx-background-radius: 20");});
            boutonDeconnexion.setOnMouseEntered(e -> {boutonDeconnexion.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color :#9AA6B2;-fx-background-radius: 20");});

            boutonDeconnexion.setPrefWidth(ApplicationSAE.width * 0.15);
        }

        BorderPane.setMargin(top, new Insets(0, 0, 20, 0));
        BorderPane.setMargin(boutonDeconnexion, new Insets(10,10,10,40));
        BorderPane.setAlignment(boutonDeconnexion, Pos.CENTER_LEFT);
        BorderPane.setAlignment(titre, Pos.CENTER);
        top.setLeft(boutonDeconnexion);
        top.setCenter(titre);
        this.setTop(top);
        this.setCenter(this.majAffichage());
        
    }
        
    public GridPane majAffichage(){
        Map<Livre, Integer> stocks = librairie.consulterStock();
        GridPane tableauStocks = new GridPane(30,30);
        
        tableauStocks.setAlignment(Pos.TOP_CENTER);
        tableauStocks.setHgap(20);
        tableauStocks.setVgap(20);
        Text ISBN = new Text("ISBN");
        Text text = new Text("Titre");
        Text qte = new Text("Quantité");
        Text add = new Text("Ajouter");
        Text remove = new Text("Retirer");

        ISBN.setTextAlignment(TextAlignment.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        qte.setTextAlignment(TextAlignment.CENTER);
        add.setTextAlignment(TextAlignment.CENTER);
        remove.setTextAlignment(TextAlignment.CENTER);

        ISBN.setFont(Font.font("Arial", 24));
        text.setFont(Font.font("Arial", 24));
        add.setFont(Font.font("Arial", 24));
        remove.setFont(Font.font("Arial", 24));
        qte.setFont(Font.font("Arial", 24));

        tableauStocks.add(ISBN, 0, 0);
        tableauStocks.add(text, 1, 0);
        tableauStocks.add(qte, 2, 0);
        tableauStocks.add(add, 3, 0);
        tableauStocks.add(remove, 4, 0);
        int cpt = 1;
        for(Livre livre : stocks.keySet()){

            Text isbn = new Text(livre.getIsbn());
            Text titree = new Text(livre.getTitre());
            Text quantite = new Text(""+stocks.get(livre));

            Button ajouter = new Button("Ajouter un livre");
            ajouter.setOnAction(new ControleurAjouterLivreTab(this, 1, livre));
            Button retirer = new Button("Retirer un livre");
            retirer.setOnAction(new ControleurRetirerLivre(this.app, this, 1, livre));

            if (ApplicationSAE.lightMode) {

                ajouter.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #9AA6B2;-fx-background-radius: 20");
                retirer.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #9AA6B2;-fx-background-radius: 20");

                ajouter.setOnMouseExited(e -> {ajouter.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #9AA6B2;-fx-background-radius: 20");});
                ajouter.setOnMouseEntered(e -> {ajouter.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color :#BCCCDC;-fx-background-radius: 20");});

                ajouter.setPrefWidth(ApplicationSAE.width * 0.2);

                retirer.setOnMouseExited(e -> {retirer.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color : #9AA6B2;-fx-background-radius: 20");});
                retirer.setOnMouseEntered(e -> {retirer.setStyle("-fx-margin: 20; -fx-padding: 15;-fx-background-color :#BCCCDC;-fx-background-radius: 20");});

                retirer.setPrefWidth(ApplicationSAE.width * 0.2);
            }

            isbn.setFont(Font.font("Arial", 24));
            titree.setFont(Font.font("Arial", 24));
            quantite.setFont(Font.font("Arial", 24));
            ajouter.setFont(Font.font("Arial", 24));
            retirer.setFont(Font.font("Arial", 24));
            isbn.setTextAlignment(TextAlignment.CENTER);
            titree.setTextAlignment(TextAlignment.CENTER);
            quantite.setTextAlignment(TextAlignment.CENTER);

            tableauStocks.add(isbn, 0, cpt);
            tableauStocks.add(titree, 1, cpt);
            tableauStocks.add(quantite, 2, cpt);
            tableauStocks.add(ajouter, 3, cpt);
            tableauStocks.add(retirer, 4, cpt);
            cpt++;
        }

        return tableauStocks;
    }

    public Librairie getLibrairie(){
        return this.librairie;
    }
}