package com.sae_java.Vue.controleur;

import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.admin.GraphiqueWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

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
