package com.taskflow.taskflow.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id); // super es para pasar dato a constructor de padre, el cual crea exception con mensaje
    }
}
