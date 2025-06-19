package com.sae_java.Vue.controleur;

import java.io.File;

import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.client.CommandeWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;

public class ControleurCommander implements EventHandler<ActionEvent>{
    
    private CommandeWindow commande;
    private ApplicationSAE app;

    public ControleurCommander(ApplicationSAE app,CommandeWindow commande){
        this.app = app;
        this.commande = commande;
    }

    @Override
    public void handle(ActionEvent e){
        
        Alert alert = new Alert(AlertType.INFORMATION);

        boolean facture = this.commande.getFacture();
        boolean commandeWorked = true;

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(this.app.getStage());
        if (selectedDirectory == null) {
            commandeWorked = this.app.getClient().commander(this.commande.getLivraison(), this.commande.getEnLigne(), false,"");
        }
        else{
            commandeWorked = this.app.getClient().commander(this.commande.getLivraison(), this.commande.getEnLigne(), facture,selectedDirectory.getAbsolutePath());
        }


        if(commandeWorked){
            alert.setContentText("La commande a été passé avec succès");
        }
        else{
            alert.setAlertType(AlertType.WARNING);
            alert.setContentText("La commande n'a pas pu être passé");
        }
        alert.showAndWait();
        this.commande.fermer();
    }

}
