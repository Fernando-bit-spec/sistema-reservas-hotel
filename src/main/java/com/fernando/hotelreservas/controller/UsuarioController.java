package com.fernando.hotelreservas.controller;


import com.fernando.hotelreservas.dto.UsuarioDTO;
import com.fernando.hotelreservas.model.Usuario.TipoUsuario;
import com.fernando.hotelreservas.repository.UsuarioRepository;
import com.fernando.hotelreservas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("totalClientes",
                usuarioRepository.findAll().stream()
                        .filter(u -> u.getTipo() == TipoUsuario.CLIENTE).count());
        model.addAttribute("totalAdmins",
                usuarioRepository.findAll().stream()
                        .filter(u -> u.getTipo() == TipoUsuario.ADMIN).count());
        return "usuarios";
    }

    @PostMapping
    public String cadastrar(@ModelAttribute UsuarioDTO.Request request,
                            RedirectAttributes redirectAttributes) {
        try {
            usuarioService.cadastrar(request);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/atualizar")
    public String atualizar(@PathVariable Long id,
                            @ModelAttribute UsuarioDTO.UpdateRequest request,
                            RedirectAttributes redirectAttributes) {
        try {
            // Se senha veio vazia, não atualiza a senha
            if (request.getSenha() != null && request.getSenha().isBlank()) {
                request.setSenha(null);
            }
            usuarioService.atualizar(id, request);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    @PostMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes redirectAttributes) {
        try {
            usuarioService.excluir(id);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/usuarios";
    }
}