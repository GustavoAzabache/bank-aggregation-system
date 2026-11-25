package com.me.backend.parser;

import java.util.List;

import org.springframework.stereotype.Component;

import com.me.backend.core.exception.UnsupportedStatementFormatException;

import reactor.core.publisher.Mono;

@Component
public class ParserFactory {

    private final List<StatementParser> parsers;

    public ParserFactory(List<StatementParser> parsers) {
        this.parsers = parsers;
    }

    public Mono<StatementParser> getParser(String filename, String contentType) {
        return Mono.justOrEmpty(
                    parsers.stream()
                        .filter(parser -> parser.supports(filename, contentType))
                        .findFirst()
                ).switchIfEmpty(Mono.error(
                    new UnsupportedStatementFormatException(
                        "No se encontró un parser compatible para el archivo: " + filename
                    )
                ));
    }
}