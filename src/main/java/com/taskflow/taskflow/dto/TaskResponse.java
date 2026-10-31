package com.taskflow.taskflow.dto;

import com.taskflow.taskflow.model.Task.TaskStatus;

import java.time.LocalDate;

public record TaskResponse(
    Long id,
    String title,
    String description,
    TaskStatus status,
    LocalDate dueDate
) {}
