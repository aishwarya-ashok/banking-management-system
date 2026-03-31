package com.aishwarya.banking.service;

import com.aishwarya.banking.dto.TransactionResponse;
import com.aishwarya.banking.entity.Account;
import com.aishwarya.banking.entity.Transaction;
import com.aishwarya.banking.exception.ResourceNotFoundException;
import com.aishwarya.banking.repository.AccountRepository;
import com.aishwarya.banking.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public List<TransactionResponse> getTransactionHistory(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        return transactionRepository.findByAccountOrderByTimestampDesc(account).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .status(transaction.getStatus())
                .timestamp(transaction.getTimestamp())
                .build();
    }
}
