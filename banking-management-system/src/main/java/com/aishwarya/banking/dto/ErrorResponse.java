package com.aishwarya.banking.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

    private LocalDateTime timestamp;

    private String message;

    private String status;
}
