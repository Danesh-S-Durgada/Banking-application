package com.company.banking;

import com.company.banking.entity.Account;
import com.company.banking.repository.AccountRepository;
import com.company.banking.service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository repository;

    @InjectMocks
    private AccountService service;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(
                "BANK1001",
                "Danesh",
                new BigDecimal("10000.00")
        );
    }

    @Test
    void shouldDepositMoney() {

        when(repository.findByAccountNumber("BANK1001"))
                .thenReturn(Optional.of(account));

        when(repository.save(account)).thenReturn(account);

        Account result = service.deposit(
                "BANK1001",
                new BigDecimal("2000.00")
        );

        assertEquals(
                new BigDecimal("12000.00"),
                result.getBalance()
        );
    }

    @Test
    void shouldWithdrawMoney() {

        when(repository.findByAccountNumber("BANK1001"))
                .thenReturn(Optional.of(account));

        when(repository.save(account)).thenReturn(account);

        Account result = service.withdraw(
                "BANK1001",
                new BigDecimal("3000.00")
        );

        assertEquals(
                new BigDecimal("7000.00"),
                result.getBalance()
        );
    }

    @Test
    void shouldRejectInsufficientBalance() {

        when(repository.findByAccountNumber("BANK1001"))
                .thenReturn(Optional.of(account));

        assertThrows(
                org.springframework.web.server.ResponseStatusException.class,
                () -> service.withdraw(
                        "BANK1001",
                        new BigDecimal("20000.00")
                )
        );

        verify(repository, never()).save(any(Account.class));
    }
}
