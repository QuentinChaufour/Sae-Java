package com.sae_java.Vue.controleur;

import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.FenetreConnexion;
import com.sae_java.Vue.admin.AdminWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public class ControleurConnexionAdmin implements EventHandler<ActionEvent>{
    
    private final FenetreConnexion fenetreConnexion;
    private final ApplicationSAE app;
    
    public ControleurConnexionAdmin(FenetreConnexion fenetreConnexion, ApplicationSAE app) {
        this.fenetreConnexion = fenetreConnexion;
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event) {
        app.getStage().setScene(new Scene(new AdminWindow(app)));
        app.getStage().sizeToScene();
        app.getStage().setTitle("Admin Window");
        app.getStage().centerOnScreen();
    }
}
