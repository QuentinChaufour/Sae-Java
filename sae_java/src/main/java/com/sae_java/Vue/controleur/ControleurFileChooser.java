package com.sae_java.Vue.controleur;

import java.io.File;

import com.sae_java.Vue.ApplicationSAE;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControleurFileChooser implements EventHandler<ActionEvent>{
    
    private ApplicationSAE app;
    private Stage stage;

    public ControleurFileChooser(ApplicationSAE app, Stage stage) {
        this.app = app;
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);

        this.app.inputDB(selectedFile);
        this.app.displayImage(1);
    }

}
