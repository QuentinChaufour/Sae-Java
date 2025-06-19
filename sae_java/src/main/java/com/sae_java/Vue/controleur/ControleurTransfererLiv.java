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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ControleurTransfererLiv implements EventHandler<ActionEvent>{
    
    private TextField isbn;
    private ChoiceBox<Librairie> librairiesSrc;
    private ChoiceBox<Librairie> librairiesTgt;
    private TextField qte;
    
    public ControleurTransfererLiv(TextField isbn, ChoiceBox<Librairie> librairiesSrc, ChoiceBox<Librairie> librairiesTgt, TextField qte){
        this.isbn = isbn;
        this.librairiesSrc = librairiesSrc; 
        this.librairiesTgt = librairiesTgt;
        this.qte = qte;
    }

    @Override
    public void handle(ActionEvent event) {
        if(!this.isbn.getText().isEmpty() && !this.qte.getText().isEmpty()){
            try {
                Livre liv = this.librairiesSrc.getValue().getLivreLib(this.isbn.getText());
                Reseau.transfert(liv, Integer.parseInt(this.qte.getText()), this.librairiesSrc.getValue().getId(), this.librairiesTgt.getValue().getId());
            } catch (NumberFormatException e) {
                System.err.println("Mauvais format du nombre");
                e.printStackTrace();
            } catch (LibraryNotFoundException e) {
                System.err.println("Librairie non trouvée");
                e.printStackTrace(); 
            } catch (SQLException e) {
                System.err.println("Connection SQL non fonctionnelle");
                e.printStackTrace();
            } catch (BookNotInStockException e) {
                System.err.println("Livre pas dans le stock");
                e.printStackTrace();
            } catch (QuantiteInvalideException e) {
                System.err.println("Quantite invalide");
                e.printStackTrace();
            }
            this.isbn.setText("");
            this.qte.setText("");
            
        }

    }
}
