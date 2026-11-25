package com.me.backend.persistence.repository;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.me.backend.core.model.Currency;
import com.me.backend.persistence.entity.TransactionEntity;

import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveCrudRepository<TransactionEntity, UUID> {
    Flux<TransactionEntity> findByAmountGreaterThanAndCurrency(BigDecimal amount, Currency currency);
}