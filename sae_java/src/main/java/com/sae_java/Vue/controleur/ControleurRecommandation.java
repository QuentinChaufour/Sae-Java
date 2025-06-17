package com.sae_java.Vue.controleur;

import java.util.List;

import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.RecommandationsWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

public class ControleurRecommandation implements EventHandler<ActionEvent>{
    
    private ApplicationSAE app;

    public ControleurRecommandation(ApplicationSAE app){
        this.app = app;
    }

    @Override
    public void handle(ActionEvent e){

        try {
            List<Livre> recommandation = this.app.getClient().OnVousRecommande(10);
            this.app.getStage().setScene(new Scene(new RecommandationsWindow(this.app, recommandation)));
        } 
        catch (LibraryNotFoundException ex) {
            // alert ?
        }

    }


}
