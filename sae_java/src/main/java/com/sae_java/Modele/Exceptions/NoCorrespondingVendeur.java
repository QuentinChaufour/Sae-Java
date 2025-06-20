package com.sae_java.Modele.Exceptions;

public class NoCorrespondingVendeur extends Exception{
    
    private final String message;

    public NoCorrespondingVendeur(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
