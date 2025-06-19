package com.sae_java.Vue.controleur;
import com.sae_java.Vue.GraphiqueWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

public class ControleurPalmaresLibrairie implements EventHandler<ActionEvent>{
    
    private GraphiqueWindow graphiqueWindow;
    private ChoiceBox<String> types;
    
    public ControleurPalmaresLibrairie(GraphiqueWindow graphiqueWindow, ChoiceBox<String> types){
        this.graphiqueWindow = graphiqueWindow;
        this.types = types;
    }

    @Override
    public void handle(ActionEvent event) {
        VBox graphique = this.graphiqueWindow.PalmaresLibrairieCambembert();
        switch (this.types.getValue()) {
            case "CAMEMBERT":
                graphique = this.graphiqueWindow.PalmaresLibrairieCambembert();
                break;
        
            case "BATON":
                graphique = this.graphiqueWindow.PalmaresLibrairieBaton();
                break;
        }
        this.graphiqueWindow.setCenter(graphique);
    }
}
