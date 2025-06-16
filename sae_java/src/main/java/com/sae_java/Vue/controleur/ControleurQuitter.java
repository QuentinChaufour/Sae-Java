package com.sae_java.Vue.controleur;

import com.sae_java.Vue.ApplicationSAE;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurQuitter implements EventHandler<ActionEvent> {

    private final ApplicationSAE app;

    public ControleurQuitter(ApplicationSAE app) {
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event) {
        
        this.app.quitter();
    }
    
}
