package com.sae_java.Vue.alert;

import javafx.scene.control.Alert;

public class DBExceptionAlert extends Alert{
    
    public DBExceptionAlert() {
        super(AlertType.ERROR);
        this.setContentText("Une erreur avec la base de donnée a eu lieu");
        this.showAndWait();
    } 

}
