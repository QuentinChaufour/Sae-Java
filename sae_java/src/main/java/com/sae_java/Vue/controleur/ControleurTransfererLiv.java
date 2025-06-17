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
import javafx.scene.control.TextField;

public class ControleurTransfererLiv implements EventHandler<ActionEvent>{
    
    private final ApplicationSAE app;
    private TextField isbn;
    private TextField idSrc;
    private TextField idTgt;
    private TextField qte;
    
    public ControleurTransfererLiv(ApplicationSAE app, TextField isbn, TextField idSrc, TextField idTgt, TextField qte){
        this.app = app;
        this.isbn = isbn;
        this.idSrc = idSrc; 
        this.idTgt = idTgt;
        this.qte = qte;
    }

    @Override
    public void handle(ActionEvent event) {
        if(!this.isbn.getText().isEmpty() && !this.idSrc.getText().isEmpty() && !this.idTgt.getText().isEmpty()){
            Livre liv;
            Librairie lib;
            try {
                lib = Reseau.getLibrairie(Integer.parseInt(this.idSrc.getText()));
                liv = lib.getLivreLib(this.isbn.getText());
                Reseau.transfert(liv, Integer.parseInt(this.qte.getText()), Integer.parseInt(this.idSrc.getText()), Integer.parseInt(this.idTgt.getText()));
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
            this.idSrc.setText("");
            this.idTgt.setText("");
            this.qte.setText("");
            
        }

    }
}
