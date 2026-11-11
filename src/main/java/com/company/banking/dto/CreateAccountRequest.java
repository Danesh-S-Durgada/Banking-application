package com.company.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank String accountNumber,
        @NotBlank String ownerName,
        @DecimalMin("0.00")
        @Digits(integer = 17, fraction = 2)
        BigDecimal initialBalance
) {}
