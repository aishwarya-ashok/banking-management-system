package com.aishwarya.banking.repository;

import com.aishwarya.banking.entity.Account;
import com.aishwarya.banking.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount(Account account);

    List<Transaction> findByAccountOrderByTimestampDesc(Account account);
}
