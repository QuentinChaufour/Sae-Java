package com.sae_java.Vue.controleur;

import com.sae_java.Modele.Exceptions.QuantiteInvalideException;
import com.sae_java.Modele.Livre;
import com.sae_java.Vue.ApplicationSAE;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ControleurAddRecommandation implements EventHandler<ActionEvent> {

    private ApplicationSAE app;
    private ChoiceBox<Livre> books;
    private TextField qteField;

    public ControleurAddRecommandation(ApplicationSAE app, ChoiceBox<Livre> book, TextField qteField) {
        this.app = app;
        this.books = book;
        this.qteField = qteField;
    }

    @Override
    public void handle(ActionEvent e) {
        try {
            Integer qte = Integer.parseInt(this.qteField.getText().trim());
            this.app.getClient().ajouterAuPanier(books.getValue(), this.app.getClient().getLibrairie(), qte);

            this.qteField.setStyle("-fx-border-color: black; -fx-border-width: 0px");
            this.qteField.setText("");
        } catch (NumberFormatException | NullPointerException | QuantiteInvalideException ex) {
            this.qteField.setText("");
            this.qteField.setStyle("-fx-border-color: red; -fx-border-width: 2px");
        }
    }
}
