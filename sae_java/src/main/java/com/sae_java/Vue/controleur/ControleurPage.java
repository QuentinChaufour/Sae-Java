package com.sae_java.Vue.controleur;

import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.ClientWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ControleurPage implements EventHandler<ActionEvent>{
    
    private final ClientWindow app;
    private final ApplicationSAE modele;

    private final Button previous;
    private final Button next;

    public ControleurPage(ClientWindow app,ApplicationSAE modele,Button previous,Button next){
        this.app = app;
        this.modele = modele;
        this.previous = previous;
        this.next = next;
    }

    @Override
    public void handle(ActionEvent e){

        Button btn = (Button) e.getSource();

        if(btn == this.previous){
            
            this.app.RedcPage();
            VBox page = this.app.createPage(this.app.getPage());
            this.app.majPage(page);
            

        }
        else if(btn == this.next){

                this.app.IncPage();

                VBox page = this.app.createPage(this.app.getPage());
                this.app.majPage(page);
        }
    }

}
