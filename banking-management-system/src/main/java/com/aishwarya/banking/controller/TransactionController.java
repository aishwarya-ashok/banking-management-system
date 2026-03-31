package com.aishwarya.banking.controller;

import com.aishwarya.banking.dto.TransactionResponse;
import com.aishwarya.banking.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@RequestParam String accountNumber) {
        List<TransactionResponse> responses = transactionService.getTransactionHistory(accountNumber);
        return ResponseEntity.ok(responses);
    }
}
