package com.me.backend.parser.dto.bcp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BcpMovementDto(
        @JsonProperty("codigo_operacion") String codigoOperacion,
        @JsonProperty("fecha_hora") LocalDateTime fechaHora,
        @JsonProperty("glosa") String glosa,
        @JsonProperty("monto") BigDecimal monto,
        @JsonProperty("tipo_movimiento") String tipoMovimiento,
        @JsonProperty("canal") String canal
) {}