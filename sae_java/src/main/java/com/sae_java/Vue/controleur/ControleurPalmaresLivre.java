package com.sae_java.Vue.controleur;
import com.sae_java.Vue.admin.GraphiqueWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

public class ControleurPalmaresLivre implements EventHandler<ActionEvent>{
    
    private GraphiqueWindow graphiqueWindow;
    private ChoiceBox<String> types;

    public ControleurPalmaresLivre(GraphiqueWindow graphiqueWindow, ChoiceBox<String> types){
        this.graphiqueWindow = graphiqueWindow;
        this.types = types;
    }

    @Override
    public void handle(ActionEvent event) {
        VBox graphique = this.graphiqueWindow.PalmaresLivreCambembert();
        switch (this.types.getValue()) {
            case "CAMEMBERT":
                graphique = this.graphiqueWindow.PalmaresLivreCambembert();
                break;
        
            case "BATON":
                graphique = this.graphiqueWindow.PalmaresLivreBaton();
                break;
        }


        this.graphiqueWindow.setCenter(graphique);
    }
}
