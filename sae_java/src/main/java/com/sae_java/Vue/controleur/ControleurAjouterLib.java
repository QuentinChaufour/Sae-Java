package com.sae_java.Vue.controleur;

import java.sql.SQLException;

import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Enumerations.EnumUpdatesDB;
import com.sae_java.Modele.Exceptions.LibraryAlreadyExistsException;
import com.sae_java.Modele.Exceptions.QuantiteInvalideException;
import com.sae_java.Modele.Librairie;
import com.sae_java.Vue.AdminWindow;
import com.sae_java.Vue.ApplicationSAE;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

public class ControleurAjouterLib implements EventHandler<ActionEvent>{
    
    private TextField titre;
    private TextField ville;
    private AdminWindow adminWindow;
    
    public ControleurAjouterLib(AdminWindow adminWindow, TextField titre, TextField ville) {
        this.titre = titre;
        this.ville = ville;
        this.adminWindow = adminWindow;
        
    }

    @Override
    public void handle(ActionEvent event) {
        if(!this.ville.getText().isEmpty() && !this.titre.getText().isEmpty()){
            try {
                for(char carac : ville.getText().toCharArray()){
                if((""+carac).matches("[0-9]")){
                    throw new QuantiteInvalideException();
                } 
                }
                if(ville.getText().matches("[0-9]")){
                    System.out.print("OIKENDFPOEJ");
                    throw new QuantiteInvalideException();
                }
                Reseau.addNewLibrairie(new Librairie(titre.getText(), ville.getText()));
            } catch (QuantiteInvalideException e) {
                System.err.println("Quantité invalide");
            } catch (SQLException e) {
                System.err.println("connection SQL échoué");
            } catch (LibraryAlreadyExistsException e) {
                System.err.println("La librairie existe déja");
            }
            this.adminWindow.getCentre().getChildren().set(1, this.adminWindow.affichageAjouterLib());
        }
    }    
}
