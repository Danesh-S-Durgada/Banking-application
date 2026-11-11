package com.company.banking.service;

import com.company.banking.dto.CreateAccountRequest;
import com.company.banking.dto.TransferRequest;
import com.company.banking.entity.Account;
import com.company.banking.repository.AccountRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Account createAccount(CreateAccountRequest request) {

        if (repository.existsByAccountNumber(request.accountNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Account already exists");
        }

        Account account = new Account(
                request.accountNumber(),
                request.ownerName(),
                request.initialBalance()
        );

        return repository.save(account);
    }

    @Transactional(readOnly = true)
    public Account getAccount(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account not found"));
    }

    @Transactional
    public Account deposit(String accountNumber, BigDecimal amount) {
        validatePositiveAmount(amount);

        Account account = getAccount(accountNumber);
        account.deposit(amount);

        return repository.save(account);
    }

    @Transactional
    public Account withdraw(String accountNumber, BigDecimal amount) {
        validatePositiveAmount(amount);

        Account account = getAccount(accountNumber);

        try {
            account.withdraw(amount);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        return repository.save(account);
    }

    @Transactional
    public void transfer(TransferRequest request) {

        validatePositiveAmount(request.amount());

        if (request.fromAccount().equals(request.toAccount())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Source and destination accounts must differ");
        }

        Account source = getAccount(request.fromAccount());
        Account destination = getAccount(request.toAccount());

        try {
            source.withdraw(request.amount());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        destination.deposit(request.amount());

        repository.save(source);
        repository.save(destination);
    }

    private void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Amount must be greater than zero");
        }
    }
}
