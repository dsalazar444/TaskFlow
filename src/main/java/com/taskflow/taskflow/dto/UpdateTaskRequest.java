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

    LocalDate dueDate,

    @NotNull(message = "Status is required")
    TaskStatus status
){}

