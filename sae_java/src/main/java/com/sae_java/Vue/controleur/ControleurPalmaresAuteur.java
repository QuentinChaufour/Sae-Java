package com.sae_java.Vue.controleur;
import com.sae_java.Vue.GraphiqueWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

public class ControleurPalmaresAuteur implements EventHandler<ActionEvent>{
    
    private GraphiqueWindow graphiqueWindow;
    private ChoiceBox<String> types;
    
    public ControleurPalmaresAuteur(GraphiqueWindow graphiqueWindow, ChoiceBox<String> types){
        this.graphiqueWindow = graphiqueWindow;
        this.types = types;
    }

    @Override
    public void handle(ActionEvent event) {
        VBox graphique = this.graphiqueWindow.PalmaresAuteurCambembert();
        switch (this.types.getValue()) {
            case "CAMEMBERT":
                graphique = this.graphiqueWindow.PalmaresAuteurCambembert();
                break;
        
            case "BATON":
                graphique = this.graphiqueWindow.PalmaresAuteurBaton();
                break;
        }
        this.graphiqueWindow.setCenter(graphique);
    }
}
