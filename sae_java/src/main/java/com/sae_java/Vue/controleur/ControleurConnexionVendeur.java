package com.sae_java.Vue.controleur;

import java.sql.SQLException;

import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Vendeur;
import com.sae_java.Modele.Exceptions.NoCorrespondingVendeur;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.FenetreConnexion;
import com.sae_java.Vue.VendeurWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

public class ControleurConnexionVendeur implements EventHandler<ActionEvent>{
    
    private final FenetreConnexion fenetreConnexion;
    private final ApplicationSAE app;
    
    public ControleurConnexionVendeur(FenetreConnexion fenetreConnexion, ApplicationSAE app) {
        this.fenetreConnexion = fenetreConnexion;
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event) {
        String nom = this.fenetreConnexion.getSellerNameField();
        String prenom = this.fenetreConnexion.getSellerFornameField();
        String mdp = this.fenetreConnexion.getSellerDMPField();
        Integer lib = this.fenetreConnexion.getChoiceBoxLibrairieSeller();

        try {
            Vendeur vendeur = Reseau.identificationVendeur(nom, prenom, mdp, lib);
            app.getStage().setScene(new Scene(new VendeurWindow(app,vendeur)));
            app.getStage().sizeToScene();
            app.getStage().setTitle("Vendeur Window");
            app.getStage().centerOnScreen();
        }
        catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors la connexion à la base de donnéees.");


        }
        catch (NoCorrespondingVendeur e) {
            showAlert(Alert.AlertType.ERROR, "Erreur Connexion", "Aucun vendeur n'a été identifié avec ces valeurs.");

        }
    }

   
    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
