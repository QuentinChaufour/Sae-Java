package com.sae_java.Vue.controleur;

import com.sae_java.Modele.Librairie;
import com.sae_java.Vue.AdminWindow;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.ClientWindow;
import com.sae_java.Vue.ConsultationLivreWindow;
import com.sae_java.Vue.FenetreConnexion;
import com.sae_java.Vue.GraphiqueWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;

public class ControleurGraphiqueWindow implements EventHandler<ActionEvent>{
    
    private final ApplicationSAE app;

    public ControleurGraphiqueWindow(ApplicationSAE app) {
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event) {
        
        app.getStage().setScene(new Scene(new GraphiqueWindow(app)));
        app.getStage().sizeToScene();
        app.getStage().setTitle("Graphique Librairie Window");
        app.getStage().centerOnScreen();
    }
}
