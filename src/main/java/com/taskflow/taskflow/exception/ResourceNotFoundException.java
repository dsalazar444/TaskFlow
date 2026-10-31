package com.taskflow.taskflow.exception;

public class ResourceNotFoundException extends RuntimeException {
     
    public ResourceNotFoundException(String resourceName, String fieldName,String fieldValue) {
        super(resourceName + " not found with" + fieldName +": " + fieldValue); // super es para pasar dato a constructor de padre, el cual crea exception con mensaje
    }
}
