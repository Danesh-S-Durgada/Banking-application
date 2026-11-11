package com.company.banking.controller;

import com.company.banking.dto.CreateAccountRequest;
import com.company.banking.dto.TransferRequest;
import com.company.banking.entity.Account;
import com.company.banking.service.AccountService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public Account createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        return service.createAccount(request);
    }

    @GetMapping("/{accountNumber}")
    public Account getAccount(
            @PathVariable String accountNumber) {
        return service.getAccount(accountNumber);
    }

    @PostMapping("/{accountNumber}/deposit")
    public Account deposit(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount) {
        return service.deposit(accountNumber, amount);
    }

    @PostMapping("/{accountNumber}/withdraw")
    public Account withdraw(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount) {
        return service.withdraw(accountNumber, amount);
    }

    @PostMapping("/transfer")
    public String transfer(
            @Valid @RequestBody TransferRequest request) {
        service.transfer(request);
        return "Transfer completed successfully";
    }
}
