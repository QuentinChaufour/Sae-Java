package com.sae_java.Vue.controleur;

import java.sql.SQLException;

import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Modele.Enumerations.EnumUpdatesDB;
import com.sae_java.Modele.Exceptions.BookNotInStockException;
import com.sae_java.Modele.Exceptions.LibraryAlreadyExistsException;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Exceptions.QuantiteInvalideException;
import com.sae_java.Modele.Librairie;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.VendeurWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ControleurTransfererLivVen implements EventHandler<ActionEvent>{
    
    private final ApplicationSAE app;
    private VendeurWindow vendeurWindow;
    private TextField isbn;
    private ChoiceBox<Librairie> idSrc;
    private TextField qte;
    
    public ControleurTransfererLivVen(ApplicationSAE app,VendeurWindow vendeurWindow, TextField isbn, ChoiceBox<Librairie> idSrc, TextField qte){
        this.app = app;
        this.vendeurWindow = vendeurWindow;
        this.isbn = isbn;
        this.idSrc = idSrc; 
        this.qte = qte;
    }

    @Override
    public void handle(ActionEvent event) {
        if(!this.isbn.getText().isEmpty() && this.idSrc.getValue() != null){
            Livre liv;
            Librairie lib;
            try {
                lib = this.idSrc.getValue();   //Reseau.getLibrairie(Integer.parseInt(this.idSrc.getValue()));
                liv = lib.getLivreLib(this.isbn.getText());
                Reseau.transfert(liv, Integer.parseInt(this.qte.getText()), lib.getId(), vendeurWindow.getVendeur().getIdLibrairie());
                showAlert(Alert.AlertType.INFORMATION, "Livre transféré avec succès.", "Le transfere a fonctionné");
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Quantité invalide", "Veuillez entrer une quantité entière positive.");
                return;
            } catch (LibraryNotFoundException e) {
                System.err.println("Librairie non trouvée");
                e.printStackTrace();
            } catch (SQLException e) {
               e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l'accès à la base de données.");
            } catch (BookNotInStockException e) {
                showAlert(Alert.AlertType.ERROR, "Livre introuvable", "Ce livre n'est pas présent dans le stock de cette librairie.");
            } catch (QuantiteInvalideException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de quantité", "La quantité spécifiée est invalide.");
            }
            this.isbn.setText("");
            this.idSrc.setValue(null);
            this.qte.setText("");
            
        }else{
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir l'ISBN et la nouvelle quantité.");
            return;

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

