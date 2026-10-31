package com.taskflow.taskflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import com.taskflow.taskflow.model.Task.TaskStatus;

public record UpdateTaskRequest (
    
    @NotBlank(message = "Title is required")
    @Size(max = 120, message = "Title cannot exceed 120 characters")
    String title,

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    String description,

    //dueDate puede ser nulo, o sin asignar
    LocalDate dueDate,

    // como taskstatus es un enum, Java ya garantiza que el valor solo puede ser uno 
    // de los estados definidos en ese enum.
    // null y no blank porque blank es para strings, y como state es un enum, no sirve.
    @NotNull(message = "Status is required")
    TaskStatus status
){}

