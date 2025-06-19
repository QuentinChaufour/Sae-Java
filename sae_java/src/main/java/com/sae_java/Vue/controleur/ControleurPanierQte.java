package com.sae_java.Vue.controleur;

import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Exceptions.PasAssezDeStockException;
import com.sae_java.Modele.Exceptions.QuantiteInvalideException;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.client.LibPanierPanel;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class ControleurPanierQte implements EventHandler<ActionEvent>{
    
    private ApplicationSAE app;
    private LibPanierPanel panel;
    private TextField qteField;
    private Livre book;

    public ControleurPanierQte(ApplicationSAE app,LibPanierPanel panel,TextField qteField,Livre book){
        this.app = app;
        this.panel = panel;
        this.qteField = qteField;
        this.book = book;
    }

    @Override
    public void handle(ActionEvent e){
        try {
                Integer qte = Integer.parseInt(this.qteField.getText());
                if(qte < 0){
                    this.app.getClient().retirerDuPanier(book, this.app.getClient().getLibrairie(), Math.abs(qte));
                    this.qteField.setStyle("");
                    this.panel.majPanel();
                }
                else{
                    this.app.getClient().ajouterAuPanier(book, this.app.getClient().getLibrairie(), Math.abs(qte));
                    this.qteField.setStyle("");
                    this.panel.majPanel();
                }
            } 
            catch (NumberFormatException | NullPointerException | PasAssezDeStockException | QuantiteInvalideException | LibraryNotFoundException ex) {
                this.qteField.setStyle("-fx-border-color: red; -fx-border-width: 1px");
            }
    }
}
