package com.sae_java.Vue;

import java.util.Map;

import javax.swing.ButtonGroup;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Vue.controleur.ControleurAjouterLivre;
import com.sae_java.Vue.controleur.ControleurDeconnexion;
import com.sae_java.Vue.controleur.ControleurRetirerLivre;
import com.sae_java.Vue.controleur.ControleurRetourAdmin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ConsultationLivreWindow extends BorderPane{

    private final ApplicationSAE app;
    private Librairie librairie;

    public ConsultationLivreWindow(ApplicationSAE app, Librairie librairie){
        this.app = app;
        this.librairie = librairie;

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
        this.setCenter(majAffichage());
        
    }
        
    public GridPane majAffichage(){
        Map<Livre, Integer> stocks = librairie.consulterStock();
        GridPane tableauStocks = new GridPane();
        
        tableauStocks.setAlignment(Pos.TOP_CENTER);
        tableauStocks.setHgap(20);
        tableauStocks.setVgap(20);
        Text ISBN = new Text("ISBN");
        Text text = new Text("Titre");
        Text qte = new Text("Quantité");
        Text add = new Text("Ajouter");
        Text remove = new Text("Retirer");

        ISBN.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        text.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        add.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        remove.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        qte.setFont(Font.font("Arial", FontWeight.NORMAL, 24));

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
            ajouter.setOnAction(new ControleurAjouterLivre(this.app, this, 1, livre));
            Button retirer = new Button("Retirer un livre");
            retirer.setOnAction(new ControleurRetirerLivre(this.app, this, 1, livre));

            isbn.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
            titree.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
            quantite.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
            ajouter.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
            retirer.setFont(Font.font("Arial", FontWeight.NORMAL, 24));

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

    

