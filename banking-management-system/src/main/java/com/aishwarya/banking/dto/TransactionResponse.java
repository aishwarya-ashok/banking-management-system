package com.aishwarya.banking.dto;

import com.aishwarya.banking.entity.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponse {

    private Long id;

    private TransactionType type;

    private BigDecimal amount;

    private String description;

    private String status;

    private LocalDateTime timestamp;
}
