package com.me.backend.ingestion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

import com.me.backend.core.model.Currency;
import com.me.backend.persistence.repository.TransactionRepository;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class StatementIngestionServiceTest {

    @Autowired
    private StatementIngestionService ingestionService;

    @Autowired
    private TransactionRepository repository;

    @BeforeEach
    void cleanDatabase() {
        // Limpiamos la tabla de forma reactiva antes de cada test
        repository.deleteAll().block();
    }

    @Test
    void shouldIngestBcpJsonFileAndPersistTransactionsInPostgres() {
        String bcpJson = """
                {
                  "entidad": "BCP",
                  "numero_cuenta": "191-98765432-0-11",
                  "moneda": "SOLES",
                  "movimientos": [
                    {
                      "codigo_operacion": "OP-1001",
                      "fecha_hora": "2026-11-10T10:00:00",
                      "glosa": "COMPRA EQUIPO COMPUTO",
                      "monto": -3500.00,
                      "tipo_movimiento": "CARGO",
                      "canal": "TRANSFERENCIA"
                    },
                    {
                      "codigo_operacion": "OP-1002",
                      "fecha_hora": "2026-11-12T18:20:00",
                      "glosa": "CENA RESTAURANTE",
                      "monto": -120.50,
                      "tipo_movimiento": "CARGO",
                      "canal": "POS"
                    },
                    {
                      "codigo_operacion": "OP-1003",
                      "fecha_hora": "2026-11-15T09:00:00",
                      "glosa": "PAGO PROYECTO FREELANCE",
                      "monto": 5200.00,
                      "tipo_movimiento": "ABONO",
                      "canal": "TRANSFERENCIA"
                    }
                  ]
                }
                """;

        DataBuffer buffer = new DefaultDataBufferFactory()
                .wrap(bcpJson.getBytes(StandardCharsets.UTF_8));

        // 1. Ejecutamos la ingesta y verificamos que guarde exactamente 3 registros
        StepVerifier.create(ingestionService.ingestStatement("bcp_noviembre.json", "application/json", Flux.just(buffer)))
                .expectNext(3L)
                .verifyComplete();

        // 2. Consultamos PostgreSQL usando el query method reactivo (> 3000 PEN)
        StepVerifier.create(repository.findByAmountGreaterThanAndCurrency(new BigDecimal("3000.00"), Currency.PEN)
                        .collectList())
                .assertNext(results -> {
                    assertEquals(2, results.size());
                    results.forEach(entity -> {
                        assertNotNull(entity.id());
                        assertEquals("BCP", entity.bankName());
                        assertEquals(Currency.PEN, entity.currency());
                    });
                })
                .verifyComplete();
    }
}