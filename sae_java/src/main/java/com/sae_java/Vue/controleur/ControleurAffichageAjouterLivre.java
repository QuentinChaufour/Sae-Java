package com.sae_java.Vue.controleur;
import com.sae_java.Vue.admin.AdminWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurAffichageAjouterLivre implements EventHandler<ActionEvent>{
    
    private AdminWindow adminWindow;

    public ControleurAffichageAjouterLivre(AdminWindow adminWindow){
        this.adminWindow = adminWindow;
    }

    @Override
    public void handle(ActionEvent event) {
        if(this.adminWindow.getCentre().getChildren().size() > 1){
            this.adminWindow.getCentre().getChildren().set(1, this.adminWindow.affichageAjouterLivre());
        } else {
            this.adminWindow.getCentre().getChildren().add(this.adminWindow.affichageAjouterLivre());
        }   


    }
}