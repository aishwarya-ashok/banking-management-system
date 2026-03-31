package com.aishwarya.banking.controller;

import com.aishwarya.banking.dto.*;
import com.aishwarya.banking.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<AccountResponse>> getMyAccounts() {
        List<AccountResponse> responses = accountService.getMyAccounts();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@Valid @RequestBody DepositWithdrawRequest request) {
        accountService.deposit(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@Valid @RequestBody DepositWithdrawRequest request) {
        accountService.withdraw(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@Valid @RequestBody TransferRequest request) {
        accountService.transfer(request);
        return ResponseEntity.ok().build();
    }
}
