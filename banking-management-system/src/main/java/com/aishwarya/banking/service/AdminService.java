package com.aishwarya.banking.service;

import com.aishwarya.banking.entity.Account;
import com.aishwarya.banking.entity.User;
import com.aishwarya.banking.repository.AccountRepository;
import com.aishwarya.banking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    public AdminService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
