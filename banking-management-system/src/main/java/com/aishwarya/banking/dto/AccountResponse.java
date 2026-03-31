package com.aishwarya.banking.dto;

import com.aishwarya.banking.entity.AccountStatus;
import com.aishwarya.banking.entity.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountResponse {

    private Long id;

    private String accountNumber;

    private BigDecimal balance;

    private AccountType accountType;

    private AccountStatus status;
}
