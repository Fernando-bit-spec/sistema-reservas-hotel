package com.fernando.hotelreservas.dto;


import com.fernando.hotelreservas.model.Usuario.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class UsuarioDTO {

    @Data
    public static class Request {
        @NotBlank(message = "Nome é obrigatório")
        private String nome;

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        private String email;

        @NotBlank(message = "Senha é obrigatória")
        private String senha;

        @NotNull(message = "Tipo é obrigatório")
        private TipoUsuario tipo;
    }

    @Data
    public static class UpdateRequest {
        private String nome;
        private String email;
        private String senha;
        private TipoUsuario tipo;
    }

    @Data
    public static class LoginRequest {
        @NotBlank(message = "Email é obrigatório")
        @Email
        private String email;

        @NotBlank(message = "Senha é obrigatória")
        private String senha;
    }

    @Data
    public static class Response {
        private Long id;
        private String nome;
        private String email;
        private TipoUsuario tipo;
    }
}