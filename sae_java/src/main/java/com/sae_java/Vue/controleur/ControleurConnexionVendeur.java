package com.sae_java.Vue.controleur;

import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.FenetreConnexion;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurConnexionVendeur implements EventHandler<ActionEvent>{
    
    private final FenetreConnexion fenetreConnexion;
    private final ApplicationSAE app;
    
    public ControleurConnexionVendeur(FenetreConnexion fenetreConnexion, ApplicationSAE app) {
        this.fenetreConnexion = fenetreConnexion;
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event) {
        System.out.println("");
    }
}
