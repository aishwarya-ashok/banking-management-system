package com.aishwarya.banking.dto;

import com.aishwarya.banking.entity.AccountType;
import lombok.Data;

@Data
public class AccountRequest {

    private AccountType accountType;
}
