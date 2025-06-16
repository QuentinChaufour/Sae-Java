package com.sae_java.Vue.controleur;

import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Enumerations.EnumUpdatesDB;
import com.sae_java.Vue.ApplicationSAE;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ControleurMajStocks implements EventHandler<ActionEvent>{
    
    private final ApplicationSAE app;
    
    public ControleurMajStocks(ApplicationSAE app) {
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event) {
        Reseau.updateInfos(EnumUpdatesDB.STOCKS);

    }
}
