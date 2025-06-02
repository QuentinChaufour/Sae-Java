package com.sae_java;

public class LibraryAlreadyExistsException extends Exception {

    /**
     * Constructeur de l'exception LibraryAlreadyExistsException
     * 
     * @param message : le message de l'exception
     */
    public LibraryAlreadyExistsException(String message) {
        super(message);
    }    
}
