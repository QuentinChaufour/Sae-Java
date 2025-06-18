package com.sae_java.Vue.controleur;

import java.sql.SQLException;

import com.sae_java.Modele.Exceptions.BookNotInStockException;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Exceptions.QuantiteInvalideException;
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.VendeurWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class ControleurAjouterLivre implements EventHandler<ActionEvent> {

    private final ApplicationSAE app;
    private final VendeurWindow vendeurWindow;
    private final TextField isbnField;
    private final TextField quantiteField;

    public ControleurAjouterLivre(ApplicationSAE app, VendeurWindow vendeurWindow, TextField isbnField, TextField quantiteField) {
        this.app = app;
        this.vendeurWindow = vendeurWindow;
        this.isbnField = isbnField;
        this.quantiteField = quantiteField;
    }

    @Override
    public void handle(ActionEvent event) {
        String isbn = isbnField.getText().trim();
        String quantiteStr = quantiteField.getText().trim();

        if (isbn.isEmpty() || quantiteStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs vides", "Veuillez remplir l'ISBN et la quantité.");
            return;
        }

        int quantite;
        try {
            quantite = Integer.parseInt(quantiteStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Quantité invalide", "Veuillez entrer un nombre entier valide pour la quantité.");
            return;
        }

        try {

            int idLibVendeur = vendeurWindow.getVendeur().getIdLibrairie();
            Librairie vendeurLib = Reseau.getLibrairie(idLibVendeur);

            Livre livre = vendeurLib.getLivreLib(isbn);
            vendeurLib.ajouterNouveauLivre(livre, quantite);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Livre ajouté au stock avec succès !");
        } catch (BookNotInStockException e) {
            showAlert(Alert.AlertType.ERROR, "Livre introuvable", "Le livre avec cet ISBN n'existe pas dans la base de données.");
        } catch (QuantiteInvalideException e) {
            showAlert(Alert.AlertType.ERROR, "Quantité invalide", "La quantité doit être un entier strictement positif.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur est survenue lors de l'accès à la base de données.");
        } catch (LibraryNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur Librairie", "Il y a une erreur lors de l'accès à la librairie du vendeur");

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
