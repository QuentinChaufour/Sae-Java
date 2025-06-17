package com.sae_java.Vue.controleur;

import java.sql.SQLException;

import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Enumerations.EnumUpdatesDB;
import com.sae_java.Modele.Exceptions.LibraryAlreadyExistsException;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Exceptions.QuantiteInvalideException;
import com.sae_java.Modele.Librairie;
import com.sae_java.Vue.ApplicationSAE;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class ControleurRetirerLib implements EventHandler<ActionEvent>{
    
    private final ApplicationSAE app;
    private TextField id;
    
    public ControleurRetirerLib(ApplicationSAE app, TextField id) {
        this.app = app;
        this.id = id;
        
    }

    @Override
    public void handle(ActionEvent event) {

        try {
            Reseau.removeLibrairie(Reseau.getLibrairie(Integer.parseInt(this.id.getText())));
        } catch (SQLException e) {
            System.err.println("connection SQL échoué");
        } catch (LibraryNotFoundException e) {
            System.err.println("La librairie n'existe pas");
        }
        this.id.setText("");

    }
}
