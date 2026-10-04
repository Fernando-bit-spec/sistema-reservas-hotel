package com.fernando.hotelreservas.controller;

import com.fernando.hotelreservas.repository.QuartoRepository;
import com.fernando.hotelreservas.repository.ReservaRepository;
import com.fernando.hotelreservas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UsuarioRepository usuarioRepository;
    private final QuartoRepository quartoRepository;
    private final ReservaRepository reservaRepository;

    /**
     * Página inicial (Dashboard pública)
     * 
     * NOTA: Esta rota é PÚBLICA por design.
     * Se você quiser protegê-la, adicione:
     * @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
     * 
     * Por enquanto, permite usuários não autenticados ver informações gerais
     * do hotel (total de usuários, quartos, reservas).
     */
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalQuartos",  quartoRepository.count());
        model.addAttribute("totalReservas", reservaRepository.count());
        return "index";
    }

    /**
     * Página de login (pública)
     * Renderiza o template de login
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
