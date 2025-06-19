package com.sae_java.Vue.controleur;

import java.sql.SQLException;

import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Exceptions.QuantiteInvalideException;
import com.sae_java.Vue.AdminWindow;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.ConsultationLivreWindow;
import com.sae_java.Vue.FenetreConnexion;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ControleurAjouterLivreAdmin implements EventHandler<ActionEvent>{
    
    private AdminWindow adminWindow;
    private TextField quantite;
    private TextField isbn;
    private TextField titre;
    private TextField editeur;
    private TextField datePublication;
    private TextField prix;
    private TextField nbPages;
    private TextField classification;
    private ChoiceBox<Librairie> librairies;
        
    public ControleurAjouterLivreAdmin(AdminWindow adminWindow, ChoiceBox<Librairie> librairies, TextField isbn, TextField titre, TextField editeur, TextField datePublication, TextField prix, TextField nbPages, TextField classification, TextField quantite) {
        this.adminWindow = adminWindow;
        this.librairies = librairies;
        this.quantite = quantite;

        this.isbn = isbn;
        this.titre = titre;
        this.editeur = editeur;
        this.datePublication = datePublication;
        this.prix = prix;
        this. nbPages = nbPages;
        this.classification = classification;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            Livre livre = new Livre(this.isbn.getText(), this.titre.getText(), this.editeur.getText(), Integer.parseInt(this.datePublication.getText()), Double.parseDouble(this.prix.getText()), Integer.parseInt(this.nbPages.getText()), this.classification.getText(), null);
            this.librairies.getValue().ajouterNouveauLivre(livre, Integer.parseInt(this.quantite.getText()));
        } catch (QuantiteInvalideException e) { 
            System.err.println("Quantité invalide");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQL pas bon");
            e.printStackTrace();
        }
            this.adminWindow.setCenter(this.adminWindow.majAffichage());
    }
}
