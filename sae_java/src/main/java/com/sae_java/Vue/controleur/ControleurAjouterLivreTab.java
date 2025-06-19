package com.sae_java.Vue.controleur;

import java.sql.SQLException;

import com.sae_java.Modele.Exceptions.QuantiteInvalideException;
import com.sae_java.Modele.Livre;
import com.sae_java.Vue.admin.ConsultationLivreWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurAjouterLivreTab implements EventHandler<ActionEvent>{
    
    private ConsultationLivreWindow consultationLivreWindow;
    private int quantite;
    private Livre livre;
    
    public ControleurAjouterLivreTab(ConsultationLivreWindow consultationLivreWindow, int quantite, Livre livre) {
        this.consultationLivreWindow = consultationLivreWindow;
        this.quantite = quantite;
        this.livre = livre;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            this.consultationLivreWindow.getLibrairie().ajouterNouveauLivre(this.livre, quantite);

        } catch (QuantiteInvalideException e) { 
            System.err.println("Quantité invalide");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("SQL pas bon");
            e.printStackTrace();
        }
            this.consultationLivreWindow.setCenter(this.consultationLivreWindow.majAffichage());
    }
}
