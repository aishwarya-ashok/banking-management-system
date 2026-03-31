package com.aishwarya.banking;

import com.aishwarya.banking.dto.DepositWithdrawRequest;
import com.aishwarya.banking.dto.TransferRequest;
import com.aishwarya.banking.entity.*;
import com.aishwarya.banking.exception.InsufficientBalanceException;
import com.aishwarya.banking.repository.AccountRepository;
import com.aishwarya.banking.repository.TransactionRepository;
import com.aishwarya.banking.repository.UserRepository;
import com.aishwarya.banking.service.AccountService;
import com.aishwarya.banking.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testDeposit() {
        User user = User.builder().id(1L).email("test@example.com").build();
        DepositWithdrawRequest request = new DepositWithdrawRequest();
        request.setAccountNumber("1234567890");
        request.setAmount(BigDecimal.valueOf(100));

        Account account = Account.builder().accountNumber("1234567890").balance(BigDecimal.ZERO).status(AccountStatus.ACTIVE).user(user).build();

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(account));

        accountService.deposit(request);

        assertEquals(BigDecimal.valueOf(100), account.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
        verify(notificationService).createNotification(user, "Deposit of 100 to account 1234567890");
    }

    @Test
    public void testWithdraw() {
        User user = User.builder().id(1L).email("test@example.com").build();
        DepositWithdrawRequest request = new DepositWithdrawRequest();
        request.setAccountNumber("1234567890");
        request.setAmount(BigDecimal.valueOf(50));

        Account account = Account.builder().accountNumber("1234567890").balance(BigDecimal.valueOf(100)).status(AccountStatus.ACTIVE).user(user).build();

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(account));

        accountService.withdraw(request);

        assertEquals(BigDecimal.valueOf(50), account.getBalance());
        verify(transactionRepository).save(any(Transaction.class));
        verify(notificationService).createNotification(user, "Withdrawal of 50 from account 1234567890");
    }

    @Test
    public void testWithdrawInsufficientBalance() {
        DepositWithdrawRequest request = new DepositWithdrawRequest();
        request.setAccountNumber("1234567890");
        request.setAmount(BigDecimal.valueOf(200));

        Account account = Account.builder().accountNumber("1234567890").balance(BigDecimal.valueOf(100)).status(AccountStatus.ACTIVE).build();

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(account));

        assertThrows(InsufficientBalanceException.class, () -> accountService.withdraw(request));
    }

    @Test
    public void testTransfer() {
        User fromUser = User.builder().id(1L).email("from@example.com").build();
        User toUser = User.builder().id(2L).email("to@example.com").build();
        TransferRequest request = new TransferRequest();
        request.setFromAccountNumber("1234567890");
        request.setToAccountNumber("0987654321");
        request.setAmount(BigDecimal.valueOf(50));

        Account fromAccount = Account.builder().accountNumber("1234567890").balance(BigDecimal.valueOf(100)).status(AccountStatus.ACTIVE).user(fromUser).build();

        Account toAccount = Account.builder().accountNumber("0987654321").balance(BigDecimal.valueOf(0)).status(AccountStatus.ACTIVE).user(toUser).build();

        when(accountRepository.findByAccountNumber("1234567890")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findByAccountNumber("0987654321")).thenReturn(Optional.of(toAccount));

        accountService.transfer(request);

        assertEquals(BigDecimal.valueOf(50), fromAccount.getBalance());
        assertEquals(BigDecimal.valueOf(50), toAccount.getBalance());
        verify(transactionRepository, times(2)).save(any(Transaction.class));
        verify(notificationService).createNotification(fromUser, "Transfer of 50 from account 1234567890 to 0987654321");
        verify(notificationService).createNotification(toUser, "Transfer of 50 to account 0987654321 from 1234567890");
    }
}
