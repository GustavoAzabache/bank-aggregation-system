package com.me.backend.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import com.me.backend.core.model.BankTransaction;
import com.me.backend.core.model.Currency;
import com.me.backend.core.model.TransactionType;

@Table("bank_transactions")
public record TransactionEntity(
        @Id UUID id,
        String bankName,
        LocalDateTime transactionDate,
        String description,
        BigDecimal amount,
        Currency currency,
        TransactionType type,
        @Transient boolean isNewRecord
) implements Persistable<UUID> {

    @PersistenceCreator
    public TransactionEntity(
            UUID id, String bankName, LocalDateTime transactionDate,
            String description, BigDecimal amount, Currency currency, TransactionType type
    ) {
        this(id, bankName, transactionDate, description, amount, currency, type, false);
    }

    public static TransactionEntity fromDomain(BankTransaction domain) {
        return new TransactionEntity(
                domain.id(),
                domain.bankName(),
                domain.transactionDate(),
                domain.description(),
                domain.amount(),
                domain.currency(),
                domain.type(),
                true
        );
    }

    public BankTransaction toDomain() {
        return new BankTransaction(
                this.id,
                this.bankName,
                this.transactionDate,
                this.description,
                this.amount,
                this.currency,
                this.type
        );
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNewRecord;
    }
}