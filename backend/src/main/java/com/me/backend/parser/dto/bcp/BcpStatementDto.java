package com.me.backend.parser.dto.bcp;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BcpStatementDto(
        @JsonProperty("entidad") String entidad,
        @JsonProperty("numero_cuenta") String numeroCuenta,
        @JsonProperty("moneda") String moneda,
        @JsonProperty("movimientos") List<BcpMovementDto> movimientos
) {}
