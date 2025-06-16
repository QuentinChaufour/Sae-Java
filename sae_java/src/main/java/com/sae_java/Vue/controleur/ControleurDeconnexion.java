package com.sae_java.Vue.controleur;

import com.sae_java.Vue.ApplicationSAE;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurDeconnexion implements EventHandler<ActionEvent>{

    private final ApplicationSAE app;

    public ControleurDeconnexion(ApplicationSAE app){
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event){

        this.app.setClient(null);

        this.app.loadIdentification();
    }
}
