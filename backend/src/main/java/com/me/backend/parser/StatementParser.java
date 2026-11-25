package com.me.backend.parser;

import org.springframework.core.io.buffer.DataBuffer;

import com.me.backend.core.model.BankTransaction;

import reactor.core.publisher.Flux;

public interface StatementParser {

    /**
     * Indica si esta estrategia soporta el archivo según su nombre/extensión o MIME type.
     */
    boolean supports(String filename, String contentType);

    /**
     * Procesa el flujo de bytes de forma no bloqueante y emite transacciones normalizadas.
     */
    Flux<BankTransaction> parse(Flux<DataBuffer> content);
}
