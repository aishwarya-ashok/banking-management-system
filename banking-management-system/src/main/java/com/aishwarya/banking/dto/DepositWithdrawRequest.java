package com.aishwarya.banking.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositWithdrawRequest {

    private String accountNumber;

    @Positive
    private BigDecimal amount;
}
