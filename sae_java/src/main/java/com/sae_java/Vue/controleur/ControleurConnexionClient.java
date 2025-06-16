package com.sae_java.Vue.controleur;

import java.sql.SQLException;

import com.sae_java.Modele.Client;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Exceptions.NoCorrespondingClient;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.ClientWindow;
import com.sae_java.Vue.FenetreConnexion;
import com.sae_java.Vue.alert.DBExceptionAlert;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public class ControleurConnexionClient implements EventHandler<ActionEvent>{
    
    private final FenetreConnexion fenetreConnexion;
    private final ApplicationSAE app;
    
    public ControleurConnexionClient(FenetreConnexion fenetreConnexion, ApplicationSAE app) {
        this.fenetreConnexion = fenetreConnexion;
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event) {

        try {
            String nom = this.fenetreConnexion.getUserNameField();
            String prenom = this.fenetreConnexion.getUserFornameField();
            String mdp = this.fenetreConnexion.getUserMDpField();
            Integer lib = this.fenetreConnexion.getChoiceBoxLibrairieUser();
            Client client = Reseau.identificationClient(nom, prenom, mdp, lib);
            this.app.setClient(client);

            app.getStage().setScene(new Scene(new ClientWindow(app, app.getClient())));
            app.getStage().sizeToScene();
            app.getStage().setTitle("Client Window");
            app.getStage().centerOnScreen();
        } 
        catch (SQLException e) {
            
            new DBExceptionAlert();
        } 
        catch (NoCorrespondingClient e) {
            
            // action 

            e.printStackTrace();
        }
    }
}
