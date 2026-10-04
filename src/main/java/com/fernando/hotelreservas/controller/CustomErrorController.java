package com.fernando.hotelreservas.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            
            model.addAttribute("status", statusCode);
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("message", "Página não encontrada (404)");
                model.addAttribute("description", "A página que você está procurando não existe.");
                return "error/404";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("message", "Acesso negado (403)");
                model.addAttribute("description", "Você não tem permissão para acessar este recurso.");
                return "error/403";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("message", "Erro interno do servidor (500)");
                model.addAttribute("description", "Algo deu errado no servidor. Tente novamente mais tarde.");
                return "error/500";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                model.addAttribute("message", "Não autenticado (401)");
                model.addAttribute("description", "Você precisa fazer login para acessar este recurso.");
                return "error/401";
            }
        }
        
        model.addAttribute("status", "Erro");
        model.addAttribute("message", "Erro desconhecido");
        model.addAttribute("description", "Não foi possível determinar o erro ocorrido.");
        return "error/error";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("status", 403);
        model.addAttribute("message", "Acesso negado");
        model.addAttribute("description", "Você não tem permissão para acessar este recurso.");
        return "error/403";
    }

    @RequestMapping("/error/404")
    public String notFound(Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("message", "Página não encontrada");
        model.addAttribute("description", "A página que você está procurando não existe.");
        return "error/404";
    }
}
