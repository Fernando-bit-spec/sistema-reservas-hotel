package com.fernando.hotelreservas.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessageUtil {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    /**
     * Retorna mensagem de erro apropriada baseada no profile
     * - DEV: mensagem detalhada
     * - PROD: mensagem genérica
     */
    public String getResourceNotFoundMessage(String resource, Long id) {
        if (isDevelopment()) {
            return resource + " não encontrado com id: " + id;
        }
        return "Recurso não encontrado";
    }

    /**
     * Retorna mensagem de erro para email duplicado
     * - DEV: email específico
     * - PROD: mensagem genérica
     */
    public String getDuplicateEmailMessage(String email) {
        if (isDevelopment()) {
            return "Email já está em uso: " + email;
        }
        return "Este email já está cadastrado no sistema";
    }

    /**
     * Retorna mensagem de erro para autenticação
     * Sempre genérica (mesmo em DEV) por razões de segurança
     */
    public String getInvalidCredentialsMessage() {
        return "Email ou senha inválidos";
    }

    private boolean isDevelopment() {
        return "dev".equalsIgnoreCase(activeProfile) || activeProfile.contains("dev");
    }
}
