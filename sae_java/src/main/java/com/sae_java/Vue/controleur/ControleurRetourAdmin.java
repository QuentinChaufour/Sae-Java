package com.sae_java.Vue.controleur;

import com.sae_java.Vue.AdminWindow;
import com.sae_java.Vue.ApplicationSAE;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class ControleurRetourAdmin implements EventHandler<ActionEvent>{

    private final ApplicationSAE app;

    public ControleurRetourAdmin(ApplicationSAE app){
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event){
        app.getStage().setScene(new Scene(new AdminWindow(app)));
        app.getStage().sizeToScene();
        app.getStage().setTitle("Admin Window");
        app.getStage().centerOnScreen();
    }
}
