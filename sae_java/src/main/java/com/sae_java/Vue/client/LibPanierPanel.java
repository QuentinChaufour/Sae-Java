package com.sae_java.Vue.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sae_java.Modele.Exceptions.LibraryNotFoundException;
import com.sae_java.Modele.Livre;
import com.sae_java.Modele.Reseau;
import com.sae_java.Vue.ApplicationSAE;
import com.sae_java.Vue.controleur.ControleurPanierQte;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class LibPanierPanel extends TitledPane{
    
    private ApplicationSAE app;

    private int page;
    private int maxPage;

    private Button backPage;
    private Button nextPage;
    private Map<Livre,Integer> content;

    public LibPanierPanel(int idLib,ApplicationSAE app,Map<Livre,Integer> content) throws LibraryNotFoundException{

        this.app = app;
        this.page = 1;
        this.content = content;
        this.maxPage = content.size()%5 == 0 ? (int)content.size()/5 : (int)Math.ceil(content.size()/5);
        
        this.setText(Reseau.getLibrairie(idLib).getNom());
        this.fontProperty().set(Font.font("arial",20));

        this.setContent(this.createPage());
        this.setStyle("-fx-padding: 10; -fx-spacing: 10;");
    }

    public void majPanel() throws LibraryNotFoundException{
        this.setContent(this.createPage());
    }

    public void nextPage(){
        if(this.page < this.maxPage){
            this.page++;
        }
    }

    public void previousPage(){
        if (this.page > 1) {
            this.page--;
        }
    }

    private VBox createPage() throws LibraryNotFoundException{

        List<Livre> livres = new ArrayList<>(this.content.keySet());

        VBox center = new VBox(10);

        for(int i = ( this.page - 1)*5;i< this.page*5;i++){
            if(i<livres.size()){
                HBox book = this.createBook(livres.get(i));
                center.getChildren().add(book);

            }
        }

        HBox pageBox = new HBox(10);

        Label pages = new Label(this.page + "");

        this.backPage = new Button();
        this.backPage.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/left_arrow_32px.png"))));

        this.nextPage = new Button();
        this.nextPage.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/right_arrow_32px.png"))));
        this.nextPage.setOnAction((ActionEvent) -> {
            this.nextPage();
            try {
                this.majPanel();
            } 
            catch (LibraryNotFoundException e) {
            }
        });

        this.backPage.setOnAction((ActionEvent) -> {
            this.previousPage();
            try {
                this.majPanel();
            } catch (LibraryNotFoundException e) {
            }
        });

        pageBox.getChildren().addAll(this.backPage, pages, this.nextPage);
        pageBox.setStyle("-fx-padding: 10;");

        pageBox.setAlignment(Pos.CENTER);
        center.getChildren().add(pageBox);

        return center;
    }

    private HBox createBook(Livre book) throws LibraryNotFoundException{
        HBox bookBox = new HBox(10);
        HBox infosBookBox = new HBox(10);
        infosBookBox.setPrefWidth(ApplicationSAE.width * 0.7);
        infosBookBox.setAlignment(Pos.CENTER_LEFT);
        bookBox.setStyle("-fx-margin:10 ;-fx-padding: 15;");
        infosBookBox.setStyle("-fx-margin:10 ;-fx-padding: 10; -fx-background-color: #d7fffb;");

        ImageView bookImage;

        if (book.getImage() == null) {
            bookImage = new ImageView(new Image(getClass().getResourceAsStream("/images/insertion_image.png")));
            bookImage.setFitHeight(96);
            bookImage.setFitWidth(96);
            bookImage.setPreserveRatio(true);
            bookImage.setSmooth(true);
        }
        else{
            bookImage = new ImageView(book.getImage());
            bookImage.setFitHeight(96);
            bookImage.setFitWidth(96);
            bookImage.setPreserveRatio(true);
            bookImage.setSmooth(true);
        }

        String bookName = book.getTitre();

        if(bookName.length() > 30){
            bookName = bookName.substring(0, 30);
            bookName += "...";
        }

        Label bookTitle = new Label(bookName);
        bookTitle.setPrefWidth(ApplicationSAE.width * 0.3);
        Label bookPrice = new Label("Prix : " + book.getPrix() + "€");
        bookPrice.setPrefWidth(ApplicationSAE.width * 0.1);
        bookPrice.setAlignment(Pos.CENTER_RIGHT);
        Label bookStock = new Label("Stock : " + Reseau.getLibrairie(this.app.getClient().getLibrairie()).consulterStock().get(book));
        bookStock.setPrefWidth(ApplicationSAE.width * 0.1);
        bookStock.setAlignment(Pos.CENTER_RIGHT);

        bookTitle.setStyle("-fx-font-size: 20px;");
        bookPrice.setStyle("-fx-font-size: 18px;");
        bookStock.setStyle("-fx-font-size: 18px;");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantité");
        quantityField.setPrefWidth(100);
        //quantityField.setAlignment(Pos.CENTER_LEFT);

        Button changeQTE = new Button("Ajouter au panier");
        changeQTE.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/ajout_16px.png"))));
        changeQTE.setAlignment(Pos.CENTER_RIGHT);
        changeQTE.setOnAction(new ControleurPanierQte(this.app, this,quantityField,book));

        HBox.setMargin(quantityField, new Insets(5));
        HBox.setMargin(changeQTE, new Insets(5));
        // pop up si nb négatif ou not a number quand ajouter au panier

        infosBookBox.getChildren().addAll(bookImage,bookTitle, bookPrice, bookStock);
        infosBookBox.setAlignment(Pos.CENTER_LEFT);

        bookBox.getChildren().addAll(infosBookBox, quantityField, changeQTE);
        bookBox.setAlignment(Pos.CENTER);

        return bookBox;
    }
}
