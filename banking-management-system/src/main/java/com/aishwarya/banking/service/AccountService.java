package com.aishwarya.banking.service;

import com.aishwarya.banking.dto.AccountRequest;
import com.aishwarya.banking.dto.AccountResponse;
import com.aishwarya.banking.dto.DepositWithdrawRequest;
import com.aishwarya.banking.dto.TransferRequest;
import com.aishwarya.banking.entity.*;
import com.aishwarya.banking.exception.AccountBlockedException;
import com.aishwarya.banking.exception.InsufficientBalanceException;
import com.aishwarya.banking.exception.ResourceNotFoundException;
import com.aishwarya.banking.repository.AccountRepository;
import com.aishwarya.banking.repository.TransactionRepository;
import com.aishwarya.banking.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final TransactionRepository transactionRepository;

    private final NotificationService notificationService;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, TransactionRepository transactionRepository, NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        User user = getCurrentUser();
        String accountNumber = generateAccountNumber();
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .accountType(request.getAccountType())
                .status(AccountStatus.ACTIVE)
                .user(user)
                .build();
        account = accountRepository.save(account);
        notificationService.createNotification(user, "Account created: " + accountNumber);
        return mapToResponse(account);
    }

    public List<AccountResponse> getMyAccounts() {
        User user = getCurrentUser();
        return accountRepository.findByUserAndStatus(user, AccountStatus.ACTIVE).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deposit(DepositWithdrawRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException("Account is not active");
        }
        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);
        Transaction transaction = Transaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .description("Deposit")
                .status("SUCCESS")
                .account(account)
                .build();
        transactionRepository.save(transaction);
        notificationService.createNotification(account.getUser(), "Deposit of " + request.getAmount() + " to account " + account.getAccountNumber());
    }

    @Transactional
    public void withdraw(DepositWithdrawRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException("Account is not active");
        }
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);
        Transaction transaction = Transaction.builder()
                .type(TransactionType.WITHDRAWAL)
                .amount(request.getAmount())
                .description("Withdrawal")
                .status("SUCCESS")
                .account(account)
                .build();
        transactionRepository.save(transaction);
        notificationService.createNotification(account.getUser(), "Withdrawal of " + request.getAmount() + " from account " + account.getAccountNumber());
    }

    @Transactional
    public void transfer(TransferRequest request) {
        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("From account not found"));
        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("To account not found"));
        if (fromAccount.getStatus() != AccountStatus.ACTIVE || toAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException("One of the accounts is not active");
        }
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        Transaction outTransaction = Transaction.builder()
                .type(TransactionType.TRANSFER_OUT)
                .amount(request.getAmount())
                .description("Transfer to " + toAccount.getAccountNumber())
                .status("SUCCESS")
                .account(fromAccount)
                .build();
        Transaction inTransaction = Transaction.builder()
                .type(TransactionType.TRANSFER_IN)
                .amount(request.getAmount())
                .description("Transfer from " + fromAccount.getAccountNumber())
                .status("SUCCESS")
                .account(toAccount)
                .build();
        transactionRepository.save(outTransaction);
        transactionRepository.save(inTransaction);
        notificationService.createNotification(fromAccount.getUser(), "Transfer of " + request.getAmount() + " from account " + fromAccount.getAccountNumber() + " to " + toAccount.getAccountNumber());
        notificationService.createNotification(toAccount.getUser(), "Transfer of " + request.getAmount() + " to account " + toAccount.getAccountNumber() + " from " + fromAccount.getAccountNumber());
    }

    private String generateAccountNumber() {
        return String.valueOf(1000000000L + (long) (Math.random() * 9000000000L));
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .build();
    }
}
