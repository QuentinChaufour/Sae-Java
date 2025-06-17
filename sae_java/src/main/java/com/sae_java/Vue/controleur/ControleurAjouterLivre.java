package com.sae_java.Vue.controleur;

import java.sql.SQLException;

import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Exceptions.QuantiteInvalideException;
import com.sae_java.Vue.AdminWindow;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.ClientWindow;
import com.sae_java.Vue.ConsultationLivreWindow;
import com.sae_java.Vue.FenetreConnexion;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public class ControleurAjouterLivre implements EventHandler<ActionEvent>{
    
    private final ApplicationSAE app;
    private ConsultationLivreWindow consultationLivreWindow;
    private int quantite;
    private Livre livre;
    
    public ControleurAjouterLivre(ApplicationSAE app, ConsultationLivreWindow consultationLivreWindow, int quantite, Livre livre) {
        this.app = app;
        this.consultationLivreWindow = consultationLivreWindow;
        this.quantite = quantite;
        this.livre = livre;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            consultationLivreWindow.getLibrairie().ajouterNouveauLivre(this.livre, quantite);
        } catch (QuantiteInvalideException e) {
            System.err.println("Quantité invalide");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQL pas bon");
            e.printStackTrace();
        }
        consultationLivreWindow.setCenter(consultationLivreWindow.majAffichage());
    }
}
