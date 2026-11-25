package com.me.backend.ingestion;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;

import com.me.backend.parser.ParserFactory;
import com.me.backend.persistence.entity.TransactionEntity;
import com.me.backend.persistence.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class StatementIngestionService {

    private final ParserFactory parserFactory;
    private final TransactionRepository repository;

    public Mono<Long> ingestStatement(String filename, String contentType, Flux<DataBuffer> content) {
        return parserFactory.getParser(filename, contentType)
                .flatMapMany(parser -> parser.parse(content))
                .map(TransactionEntity::fromDomain)
                .buffer(500)
                .flatMap(repository::saveAll)
                .count();
    }
}