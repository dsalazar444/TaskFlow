package com.taskflow.taskflow.dto;

public record AuthResponse(
    String accessToken,
    String tokenType
) {}