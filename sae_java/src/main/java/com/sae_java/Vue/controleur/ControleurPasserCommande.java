package com.sae_java.Vue.controleur;

import com.sae_java.Modele.Exceptions.BookNotInStockException;
import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Exceptions.QuantiteInvalideException;
import com.sae_java.Modele.Client;
import com.sae_java.Modele.Commande;
import com.sae_java.Modele.Librairie;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Panier;
import com.sae_java.Modele.Reseau;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.VendeurWindow;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;


import java.sql.SQLException;


public class ControleurPasserCommande implements EventHandler<ActionEvent> {

    private final ApplicationSAE app;
    private VendeurWindow vendeurWindow;
    private TextField clientId;
    private TextField isbnCommande;
    private TextField qteCommande;

    public ControleurPasserCommande(ApplicationSAE app, VendeurWindow vendeurWindow, TextField clientId, TextField isbnCommande, TextField qteCommande) {
        this.app = app;
        this.vendeurWindow = vendeurWindow;
        this.clientId = clientId;
        this.isbnCommande = isbnCommande;
        this.qteCommande = qteCommande;
    }

    @Override
    public void handle(ActionEvent event) {
        String isbn = isbnCommande.getText().trim();
        String clientIdStr = clientId.getText().trim();
        String qteStr = qteCommande.getText().trim();

        if (isbn.isEmpty() || clientIdStr.isEmpty() || qteStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir l'ISBN, l'ID client et la quantité.");
            return;
        }
        try {
            int idClientint = Integer.parseInt(clientId.getText());
            int qteint = Integer.parseInt(qteCommande.getText());

            int idLibrairie = vendeurWindow.getVendeur().getIdLibrairie();
            Librairie librairie = Reseau.getLibrairie(idLibrairie);
            Livre livre = librairie.getLivreLib(isbn);
            Client client = Reseau.trouverClient(idClientint);
            client.setLibrairie(idLibrairie);
            try{
                client.ajouterAuPanier(livre, idLibrairie, qteint);
                String modeLivraison = "M";      
                client.commander(modeLivraison,true,true,".");
                showAlert(Alert.AlertType.INFORMATION, "Commande réussite.", "La commande a était passéé avec succès");
            }catch(QuantiteInvalideException e){
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur Panier", "Erreur lors de l’accès au panier du client.");
            }     
        } catch (BookNotInStockException e) {
            showAlert(Alert.AlertType.INFORMATION, "Non disponible", "Ce livre n’est pas en stock dans cette librairie.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de l’accès à la base de données.");
        } catch (LibraryNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur Librairie", "Il y a une erreur lors de l'accès à la librairie du vendeur");
        }
            
        this.clientId.setText("");
        this.isbnCommande.setText("");
        this.qteCommande.setText("");
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
