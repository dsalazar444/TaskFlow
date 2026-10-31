package com.taskflow.taskflow.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // sirve para centralizar el manejo de excepciones de todos nuestros Controllers en una sola clase.
// cuando se lance exception, spring vendrá acá a ver si hay un handler
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }   
    
    // handler para exception lanzada por @valid, en controller con DTO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        // <nombre atributo con error, mensaje de error definido en dtos>

        //getBinding.. obtiene resultado de validación de DTO, getFieldErrors obtiene 
        // aquellos campos que obtuvieron error, para cada uno, lo añadimos al map, su field y 
        // su message, definido en DTO
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }
}

