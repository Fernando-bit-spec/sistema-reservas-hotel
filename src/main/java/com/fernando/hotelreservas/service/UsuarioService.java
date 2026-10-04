package com.fernando.hotelreservas.service;


import com.fernando.hotelreservas.dto.UsuarioDTO;
import com.fernando.hotelreservas.exception.BusinessException;
import com.fernando.hotelreservas.exception.ResourceNotFoundException;
import com.fernando.hotelreservas.model.Usuario;
import com.fernando.hotelreservas.repository.UsuarioRepository;
import com.fernando.hotelreservas.util.ErrorMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ErrorMessageUtil errorMessageUtil;

    @Transactional
    public UsuarioDTO.Response cadastrar(UsuarioDTO.Request request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(errorMessageUtil.getDuplicateEmailMessage(request.getEmail()));
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .tipo(request.getTipo())
                .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    public UsuarioDTO.Response login(UsuarioDTO.LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(errorMessageUtil.getInvalidCredentialsMessage()));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new BusinessException(errorMessageUtil.getInvalidCredentialsMessage());
        }

        return toResponse(usuario);
    }

    public List<UsuarioDTO.Response> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UsuarioDTO.Response buscarPorId(Long id) {
        return toResponse(findById(id));
    }

    @Transactional
    public UsuarioDTO.Response atualizar(Long id, UsuarioDTO.UpdateRequest request) {
        Usuario usuario = findById(id);

        if (request.getNome() != null) usuario.setNome(request.getNome());
        if (request.getSenha() != null && !request.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }
        if (request.getTipo() != null) usuario.setTipo(request.getTipo());
        if (request.getEmail() != null) {
            if (!request.getEmail().equals(usuario.getEmail())
                    && usuarioRepository.existsByEmail(request.getEmail())) {
                throw new BusinessException(errorMessageUtil.getDuplicateEmailMessage(request.getEmail()));
            }
            usuario.setEmail(request.getEmail());
        }

        return toResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    public void excluir(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException(errorMessageUtil.getResourceNotFoundMessage("Usuário", id));
        }
        usuarioRepository.deleteById(id);
    }

    private Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(errorMessageUtil.getResourceNotFoundMessage("Usuário", id)));
    }

    private UsuarioDTO.Response toResponse(Usuario usuario) {
        UsuarioDTO.Response response = new UsuarioDTO.Response();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setTipo(usuario.getTipo());
        return response;
    }
}