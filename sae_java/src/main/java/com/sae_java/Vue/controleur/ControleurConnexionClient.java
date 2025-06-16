package com.sae_java.Vue.controleur;

import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.ClientWindow;
import com.sae_java.Vue.FenetreConnexion;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public class ControleurConnexionClient implements EventHandler<ActionEvent>{
    
    private final FenetreConnexion fenetreConnexion;
    private final ApplicationSAE app;
    
    public ControleurConnexionClient(FenetreConnexion fenetreConnexion, ApplicationSAE app) {
        this.fenetreConnexion = fenetreConnexion;
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event) {
        app.getStage().setScene(new Scene(new ClientWindow(app,app.getClient())));
        app.getStage().sizeToScene();
        app.getStage().setTitle("Client Window");
        app.getStage().centerOnScreen();
    }
}
