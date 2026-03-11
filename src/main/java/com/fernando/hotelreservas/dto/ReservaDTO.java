package com.fernando.hotelreservas.dto;


import com.fernando.hotelreservas.model.Reserva.StatusReserva;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

public class ReservaDTO {

    @Data
    public static class Request {
        @NotNull(message = "Data de entrada é obrigatória")
        private LocalDate dataEntrada;

        @NotNull(message = "Data de saída é obrigatória")
        private LocalDate dataSaida;

        @NotNull(message = "Usuário é obrigatório")
        private Long usuarioId;

        @NotNull(message = "Quarto é obrigatório")
        private Long quartoId;
    }

    @Data
    public static class Response {
        private Long id;
        private LocalDate dataEntrada;
        private LocalDate dataSaida;
        private StatusReserva status;
        private Long usuarioId;
        private String nomeUsuario;
        private Long quartoId;
        private String numeroQuarto;
    }
}