package com.sae_java.Vue.controleur;

import java.sql.SQLException;
import java.util.Optional;
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Vue.AdminWindow;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;

public class ControleurRetirerLib implements EventHandler<ActionEvent>{
    
    private ChoiceBox<Librairie> librairies;
    private AdminWindow adminWindow;
    
    public ControleurRetirerLib(AdminWindow adminWindow, ChoiceBox<Librairie> librairies) {
        this.librairies = librairies;
        this.adminWindow = adminWindow;
    }

    @Override
    public void handle(ActionEvent event) {
        Alert attention = new Alert(AlertType.CONFIRMATION,"Êtes-vous sûr de vouloir supprimmer la librairie ?\n Cela supprimera toutes les commandes et stocks de la librairie", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> reponse = attention.showAndWait();
        if(reponse.isPresent() && reponse.get().equals(ButtonType.YES)){
            try {
                Reseau.removeLibrairie(Reseau.getLibrairie(this.librairies.getValue().getId()));
                this.adminWindow.getCentre().getChildren().set(1, this.adminWindow.affichageRetirerLib());
            } catch (SQLException e) {
                System.err.println("connection SQL échoué");
            } catch (LibraryNotFoundException e) {
                System.err.println("La librairie n'existe pas");
            }
        }
    }
}
