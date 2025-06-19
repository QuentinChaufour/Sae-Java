package com.sae_java.Vue.controleur;
import com.sae_java.Vue.AdminWindow;
import com.sae_java.Vue.GraphiqueWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

public class ControleurAffichageRetirerLib implements EventHandler<ActionEvent>{
    
    private AdminWindow adminWindow;

    public ControleurAffichageRetirerLib(AdminWindow adminWindow){
        this.adminWindow = adminWindow;
    }

    @Override
    public void handle(ActionEvent event) {
        if(this.adminWindow.getCentre().getChildren().size() > 1){
            this.adminWindow.getCentre().getChildren().set(1, this.adminWindow.affichageRetirerLib());
        } else {
            this.adminWindow.getCentre().getChildren().add(this.adminWindow.affichageRetirerLib());
        }   


    }
}