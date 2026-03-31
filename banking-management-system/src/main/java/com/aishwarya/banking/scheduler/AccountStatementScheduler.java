package com.aishwarya.banking.scheduler;

import com.aishwarya.banking.entity.Account;
import com.aishwarya.banking.entity.AccountStatus;
import com.aishwarya.banking.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class AccountStatementScheduler {

    private static final Logger logger = LoggerFactory.getLogger(AccountStatementScheduler.class);

    private final AccountRepository accountRepository;

    public AccountStatementScheduler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Scheduled(cron = "0 0 9 * * ?")
    public void generateAccountSummary() {
        List<Account> activeAccounts = accountRepository.findByStatus(AccountStatus.ACTIVE);
        for (Account account : activeAccounts) {
            logger.info("Account Summary - Account Number: {}, Balance: {}", account.getAccountNumber(), account.getBalance());
        }
    }
}
