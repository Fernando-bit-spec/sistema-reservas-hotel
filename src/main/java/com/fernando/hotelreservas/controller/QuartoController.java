package com.fernando.hotelreservas.controller;

import com.fernando.hotelreservas.dto.QuartoDTO;
import com.fernando.hotelreservas.model.Quarto.StatusQuarto;
import com.fernando.hotelreservas.repository.QuartoRepository;
import com.fernando.hotelreservas.service.QuartoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/quartos")
@RequiredArgsConstructor
public class QuartoController {

    private final QuartoService quartoService;
    private final QuartoRepository quartoRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("quartos", quartoService.listarTodos());
        model.addAttribute("quartosDisponiveis",
                quartoRepository.findByStatus(StatusQuarto.DISPONIVEL).size());
        model.addAttribute("quartosOcupados",
                quartoRepository.findByStatus(StatusQuarto.OCUPADO).size());
        model.addAttribute("quartosManutencao",
                quartoRepository.findByStatus(StatusQuarto.MANUTENCAO).size());
        return "quartos";
    }

    @PostMapping
    public String cadastrar(@ModelAttribute QuartoDTO.Request request,
                            RedirectAttributes redirectAttributes) {
        try {
            quartoService.cadastrar(request);
            redirectAttributes.addFlashAttribute("sucesso", "Quarto cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/quartos";
    }

    @PostMapping("/{id}/atualizar")
    public String atualizar(@PathVariable Long id,
                            @ModelAttribute QuartoDTO.UpdateRequest request,
                            RedirectAttributes redirectAttributes) {
        try {
            quartoService.atualizar(id, request);
            redirectAttributes.addFlashAttribute("sucesso", "Quarto atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/quartos";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        try {
            quartoService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Quarto excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/quartos";
    }
}