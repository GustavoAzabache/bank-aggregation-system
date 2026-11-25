package com.me.backend.parser.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

import com.me.backend.core.model.Currency;
import com.me.backend.core.model.TransactionType;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import tools.jackson.databind.ObjectMapper;

class JsonStatementParserTest {

    private JsonStatementParser parser;

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        parser = new JsonStatementParser(mapper);
    }

    @Test
    void shouldParseBcpJsonStatementAndIgnoreExtraFields() {
        String bcpJson = """
                {
                  "entidad": "BCP",
                  "numero_cuenta": "191-98765432-0-11",
                  "titular": "GUSTAVO AZABACHE",
                  "moneda": "SOLES",
                  "saldo_disponible": 4520.50,
                  "movimientos": [
                    {
                      "codigo_operacion": "OP-998811",
                      "fecha_hora": "2026-10-15T14:30:00",
                      "glosa": "COMPRA POS WONG LARCOMAR",
                      "monto": -245.90,
                      "tipo_movimiento": "CARGO",
                      "canal": "POS",
                      "sucursal": "LIMA-MIRAFLORES"
                    },
                    {
                      "codigo_operacion": "OP-998812",
                      "fecha_hora": "2026-10-16T09:15:00",
                      "glosa": "ABONO HABERES EMPRESA",
                      "monto": 4500.00,
                      "tipo_movimiento": "ABONO",
                      "canal": "TRANSFERENCIA",
                      "sucursal": "CENTRAL"
                    }
                  ]
                }
                """;

        DataBuffer buffer = new DefaultDataBufferFactory()
                .wrap(bcpJson.getBytes(StandardCharsets.UTF_8));

        StepVerifier.create(parser.parse(Flux.just(buffer)))
                .assertNext(tx -> {
                    assertEquals("BCP", tx.bankName());
                    assertEquals("COMPRA POS WONG LARCOMAR", tx.description());
                    assertEquals(new BigDecimal("245.90"), tx.amount());
                    assertEquals(Currency.PEN, tx.currency());
                    assertEquals(TransactionType.EXPENSE, tx.type());
                })
                .assertNext(tx -> {
                    assertEquals("BCP", tx.bankName());
                    assertEquals("ABONO HABERES EMPRESA", tx.description());
                    assertEquals(new BigDecimal("4500.00"), tx.amount());
                    assertEquals(Currency.PEN, tx.currency());
                    assertEquals(TransactionType.INCOME, tx.type());
                })
                .verifyComplete();
    }
}