package com.me.backend.core.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BankTransaction(
        UUID id,
        String bankName,
        LocalDateTime transactionDate,
        String description,
        BigDecimal amount,
        Currency currency,
        TransactionType type
) {
    public static BankTransaction create(
            String bankName,
            LocalDateTime transactionDate,
            String description,
            BigDecimal amount,
            Currency currency,
            TransactionType type
    ) {
        return new BankTransaction(
                UUID.randomUUID(),
                bankName,
                transactionDate,
                description,
                amount,
                currency,
                type
        );
    }
}