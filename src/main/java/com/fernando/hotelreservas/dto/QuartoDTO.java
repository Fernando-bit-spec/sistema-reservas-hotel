package com.fernando.hotelreservas.dto;


import com.fernando.hotelreservas.model.Quarto.StatusQuarto;
import com.fernando.hotelreservas.model.Quarto.TipoQuarto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

public class QuartoDTO {

    @Data
    public static class Request {
        @NotBlank(message = "Número é obrigatório")
        private String numero;

        @NotNull(message = "Tipo é obrigatório")
        private TipoQuarto tipo;

        @NotNull(message = "Preço é obrigatório")
        @Positive(message = "Preço deve ser positivo")
        private BigDecimal preco;

        @NotNull(message = "Status é obrigatório")
        private StatusQuarto status;

        private Long hotelId;
    }

    @Data
    public static class UpdateRequest {
        private String numero;
        private TipoQuarto tipo;
        private BigDecimal preco;
        private StatusQuarto status;
        private Long hotelId;
    }

    @Data
    public static class DisponibilidadeRequest {
        @NotNull(message = "Data de entrada é obrigatória")
        private LocalDate dataEntrada;

        @NotNull(message = "Data de saída é obrigatória")
        private LocalDate dataSaida;
    }

    @Data
    public static class Response {
        private Long id;
        private String numero;
        private TipoQuarto tipo;
        private BigDecimal preco;
        private StatusQuarto status;
        private Long hotelId;
    }
}