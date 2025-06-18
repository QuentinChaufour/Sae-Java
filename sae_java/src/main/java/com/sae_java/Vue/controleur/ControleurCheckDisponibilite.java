package com.sae_java.Vue.controleur;

import com.sae_java.Modele.Exceptions.BookNotInStockException;
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.VendeurWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class ControleurCheckDisponibilite implements EventHandler<ActionEvent> {

    private final ApplicationSAE app;
    private final VendeurWindow vendeurWindow;
    private final TextField isbnField;

    public ControleurCheckDisponibilite(ApplicationSAE app, VendeurWindow vendeurWindow, TextField isbnField) {
        this.app = app;
        this.vendeurWindow = vendeurWindow;
        this.isbnField = isbnField;
    }

    @Override
    public void handle(ActionEvent event) {
        String isbn = isbnField.getText().trim();

        if (isbn.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champ vide", "Veuillez entrer un ISBN.");
            return;
        }

        try {
            int idLibrairie = vendeurWindow.getVendeur().getIdLibrairie();
            Librairie librairie = Reseau.getLibrairie(idLibrairie);
            Livre livre = librairie.getLivreLib(isbn);

            int quantite = librairie.consulterStock().get(livre);
            showAlert(Alert.AlertType.INFORMATION, "Livre disponible",
                    "Le livre est en stock avec " + quantite + " exemplaire(s).");

        } catch (BookNotInStockException e) {
            showAlert(Alert.AlertType.INFORMATION, "Non disponible", "Ce livre n’est pas en stock dans cette librairie.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l’accès à la base de données.");
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
