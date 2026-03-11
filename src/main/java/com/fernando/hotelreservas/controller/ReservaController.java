package com.fernando.hotelreservas.controller;

import com.fernando.hotelreservas.dto.ReservaDTO;
import com.fernando.hotelreservas.model.Reserva.StatusReserva;
import com.fernando.hotelreservas.repository.ReservaRepository;
import com.fernando.hotelreservas.service.QuartoService;
import com.fernando.hotelreservas.service.ReservaService;
import com.fernando.hotelreservas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private final UsuarioService usuarioService;
    private final QuartoService quartoService;
    private final ReservaRepository reservaRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("reservas",  reservaService.listarTodas());
        model.addAttribute("usuarios",  usuarioService.listarTodos());
        model.addAttribute("quartos",   quartoService.listarDisponiveis());
        model.addAttribute("totalConfirmadas",
                reservaRepository.findByStatus(StatusReserva.CONFIRMADA).size());
        model.addAttribute("totalPendentes",
                reservaRepository.findByStatus(StatusReserva.PENDENTE).size());
        model.addAttribute("totalCanceladas",
                reservaRepository.findByStatus(StatusReserva.CANCELADA).size());
        return "reservas";
    }

    @PostMapping
    public String criar(@ModelAttribute ReservaDTO.Request request,
                        RedirectAttributes redirectAttributes) {
        try {
            reservaService.criar(request);
            redirectAttributes.addFlashAttribute("sucesso", "Reserva criada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/reservas";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id,
                           RedirectAttributes redirectAttributes) {
        try {
            reservaService.cancelar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Reserva cancelada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/reservas";
    }
}