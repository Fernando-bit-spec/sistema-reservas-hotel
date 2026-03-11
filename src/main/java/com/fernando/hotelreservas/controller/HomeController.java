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

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalUsuarios", usuarioRepository.count());
        model.addAttribute("totalQuartos",  quartoRepository.count());
        model.addAttribute("totalReservas", reservaRepository.count());
        return "index";
    }
}