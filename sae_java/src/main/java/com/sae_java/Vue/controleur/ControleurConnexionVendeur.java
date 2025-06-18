package com.sae_java.Vue.controleur;

import com.sae_java.Modele.Vendeur;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.ClientWindow;
import com.sae_java.Vue.FenetreConnexion;
import com.sae_java.Vue.VendeurWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public class ControleurConnexionVendeur implements EventHandler<ActionEvent>{
    
    private final FenetreConnexion fenetreConnexion;
    private final ApplicationSAE app;
    
    public ControleurConnexionVendeur(FenetreConnexion fenetreConnexion, ApplicationSAE app) {
        this.fenetreConnexion = fenetreConnexion;
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event) {
        app.getStage().setScene(new Scene(new VendeurWindow(app,new Vendeur("Bonjoux", "Lucie", "12 rue des Lilas", 1, 3))));
        app.getStage().sizeToScene();
        app.getStage().setTitle("Vendeur Window");
        app.getStage().centerOnScreen();
    }
}
