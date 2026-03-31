package com.aishwarya.banking.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {

    private String fromAccountNumber;

    private String toAccountNumber;

    @Positive
    private BigDecimal amount;
}
