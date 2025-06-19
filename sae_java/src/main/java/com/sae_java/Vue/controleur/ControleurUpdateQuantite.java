package com.sae_java.Vue.controleur;

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

import java.sql.SQLException;

public class ControleurUpdateQuantite implements EventHandler<ActionEvent> {

    private final ApplicationSAE app;
    private final VendeurWindow vendeurWindow;
    private final TextField isbnField;
    private final TextField quantiteField;

    public ControleurUpdateQuantite(ApplicationSAE app, VendeurWindow vendeurWindow, TextField isbnField, TextField quantiteField) {
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
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir l'ISBN et la nouvelle quantité.");
            return;
        }

        int nouvelleQuantite;
        try {
            nouvelleQuantite = Integer.parseInt(quantiteStr);
            if (nouvelleQuantite < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Quantité invalide", "Veuillez entrer une quantité entière positive.");
            return;
        }

        try {
            int idLibrairie = vendeurWindow.getVendeur().getIdLibrairie();
            Librairie librairie = Reseau.getLibrairie(idLibrairie);
            Livre livre = librairie.getLivreLib(isbn);

            int ancienneQuantite = librairie.consulterStock().get(livre);

            if (nouvelleQuantite == ancienneQuantite) {
                showAlert(Alert.AlertType.INFORMATION, "Aucune modification", "La quantité est déjà à jour.");
                return;
            }

            if (nouvelleQuantite < ancienneQuantite) {
                int diff = ancienneQuantite - nouvelleQuantite;
                librairie.retirerLivre(livre, diff);
            } else {
                int diff = nouvelleQuantite - ancienneQuantite;
                librairie.ajouterAuStock(livre, diff);
            }

            showAlert(Alert.AlertType.INFORMATION, "Quantité mise à jour", "La quantité a été modifiée avec succès.");

        } catch (BookNotInStockException e) {
            showAlert(Alert.AlertType.ERROR, "Livre introuvable", "Ce livre n'est pas présent dans le stock de cette librairie.");
        } catch (QuantiteInvalideException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de quantité", "La quantité spécifiée est invalide.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'accès à la base de données.");
        } catch (LibraryNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
