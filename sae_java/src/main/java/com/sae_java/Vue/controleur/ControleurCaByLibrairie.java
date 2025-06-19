package com.sae_java.Vue.controleur;

import com.sae_java.Vue.admin.GraphiqueWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.VBox;

public class ControleurCaByLibrairie implements EventHandler<ActionEvent>{
    
    private GraphiqueWindow graphiqueWindow;
    private ChoiceBox<String> types;
    
    public ControleurCaByLibrairie(GraphiqueWindow graphiqueWindow, ChoiceBox<String> types){
        this.graphiqueWindow = graphiqueWindow;
        this.types = types;
    }

    @Override
    public void handle(ActionEvent event) {
        VBox graphique = this.graphiqueWindow.CAByLibrairieCambembert();
        switch (this.types.getValue()) {
            case "CAMEMBERT":
                graphique = this.graphiqueWindow.CAByLibrairieCambembert();
                break;
        
            case "BATON":
                graphique = this.graphiqueWindow.CAByLibrairieBaton();
                break;
        }
        this.graphiqueWindow.setCenter(graphique);
    }
}
