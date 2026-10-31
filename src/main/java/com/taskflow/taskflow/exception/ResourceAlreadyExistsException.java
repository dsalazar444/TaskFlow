package com.taskflow.taskflow.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(
            String resourceName,
            String fieldName
    ) {
        super(resourceName + " with this " + fieldName +
        " already exists. Please choose another value.");
    }
}