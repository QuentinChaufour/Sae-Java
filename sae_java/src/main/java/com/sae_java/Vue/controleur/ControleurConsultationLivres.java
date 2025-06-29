package com.sae_java.Vue.controleur;

import com.sae_java.Modele.Librairie;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.admin.ConsultationLivreWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;

public class ControleurConsultationLivres implements EventHandler<ActionEvent>{
    
    private final ApplicationSAE app;
    private ChoiceBox<Librairie> librairies;
    
    public ControleurConsultationLivres(ApplicationSAE app, ChoiceBox<Librairie> librairies) {
        this.app = app;
        this.librairies = librairies;
    }

    @Override
    public void handle(ActionEvent event) {
        
        app.getStage().setScene(new Scene(new ConsultationLivreWindow(app, this.librairies.getValue())));
        app.getStage().sizeToScene();
        app.getStage().setTitle("Consultation Livres Window");
        app.getStage().centerOnScreen();
    }
}
