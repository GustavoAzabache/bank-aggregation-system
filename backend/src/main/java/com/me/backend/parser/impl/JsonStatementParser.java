package com.me.backend.parser.impl;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;

import com.me.backend.core.model.BankTransaction;
import com.me.backend.core.model.Currency;
import com.me.backend.core.model.TransactionType;
import com.me.backend.parser.StatementParser;
import com.me.backend.parser.dto.bcp.BcpStatementDto;
import com.me.backend.parser.util.DataBufferUtilsHelper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class JsonStatementParser implements StatementParser {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(String filename, String contentType) {
        boolean isJsonExt = filename != null && filename.toLowerCase().endsWith(".json");
        boolean isJsonMime = contentType != null && contentType.contains("application/json");
        return isJsonExt || isJsonMime;
    }

    @Override
    public Flux<BankTransaction> parse(Flux<DataBuffer> content) {
        return DataBufferUtilsHelper.fluxToString(content)
                .publishOn(Schedulers.boundedElastic())
                .flatMapIterable(rawJson -> {
                    try {
                        BcpStatementDto statement = objectMapper.readValue(rawJson, BcpStatementDto.class);
                        String bankName = statement.entidad() != null ? statement.entidad() : "BCP";
                        Currency currency = parseCurrency(statement.moneda());

                        return statement.movimientos().stream()
                                .map(mov -> BankTransaction.create(
                                        bankName,
                                        mov.fechaHora(),
                                        mov.glosa(),
                                        mov.monto().abs(),
                                        currency,
                                        parseType(mov.tipoMovimiento())
                                ))
                                .toList();
                    } catch (Exception e) {
                        throw new RuntimeException("Error parseando extracto JSON del BCP", e);
                    }
                });
    }

    private Currency parseCurrency(String moneda) {
        if ("USD".equalsIgnoreCase(moneda) || "DOLARES".equalsIgnoreCase(moneda)) {
            return Currency.USD;
        }
        return Currency.PEN;
    }

    private TransactionType parseType(String tipo) {
        if ("ABONO".equalsIgnoreCase(tipo)) {
            return TransactionType.INCOME;
        }
        return TransactionType.EXPENSE;
    }
}