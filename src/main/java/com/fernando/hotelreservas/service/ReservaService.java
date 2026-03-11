package com.fernando.hotelreservas.service;


import com.fernando.hotelreservas.dto.ReservaDTO;
import com.fernando.hotelreservas.exception.BusinessException;
import com.fernando.hotelreservas.exception.ResourceNotFoundException;
import com.fernando.hotelreservas.model.Quarto;
import com.fernando.hotelreservas.model.Reserva;
import com.fernando.hotelreservas.model.Reserva.StatusReserva;
import com.fernando.hotelreservas.model.Usuario;
import com.fernando.hotelreservas.repository.ReservaRepository;
import com.fernando.hotelreservas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final QuartoService quartoService;

    @Transactional
    public ReservaDTO.Response criar(ReservaDTO.Request request) {
        if (!request.getDataSaida().isAfter(request.getDataEntrada())) {
            throw new BusinessException("Data de saída deve ser após a data de entrada");
        }

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado com id: " + request.getUsuarioId()));

        Quarto quarto = quartoService.findById(request.getQuartoId());

        if (quarto.getStatus() == Quarto.StatusQuarto.MANUTENCAO) {
            throw new BusinessException("Quarto em manutenção, não pode ser reservado");
        }

        boolean conflito = reservaRepository.existsConflito(
                quarto.getId(), request.getDataEntrada(), request.getDataSaida());

        if (conflito) {
            throw new BusinessException("Quarto não disponível para o período selecionado");
        }

        Reserva reserva = Reserva.builder()
                .dataEntrada(request.getDataEntrada())
                .dataSaida(request.getDataSaida())
                .status(StatusReserva.CONFIRMADA)
                .usuario(usuario)
                .quarto(quarto)
                .build();

        return toResponse(reservaRepository.save(reserva));
    }

    @Transactional
    public ReservaDTO.Response cancelar(Long id) {
        Reserva reserva = findById(id);

        if (reserva.getStatus() == StatusReserva.CANCELADA) {
            throw new BusinessException("Reserva já está cancelada");
        }
        if (reserva.getStatus() == StatusReserva.CONCLUIDA) {
            throw new BusinessException("Não é possível cancelar uma reserva concluída");
        }

        reserva.setStatus(StatusReserva.CANCELADA);
        return toResponse(reservaRepository.save(reserva));
    }

    public List<ReservaDTO.Response> listarPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuário não encontrado com id: " + usuarioId);
        }
        return reservaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ReservaDTO.Response> listarTodas() {
        return reservaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ReservaDTO.Response buscarPorId(Long id) {
        return toResponse(findById(id));
    }

    private Reserva findById(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada com id: " + id));
    }

    private ReservaDTO.Response toResponse(Reserva reserva) {
        ReservaDTO.Response response = new ReservaDTO.Response();
        response.setId(reserva.getId());
        response.setDataEntrada(reserva.getDataEntrada());
        response.setDataSaida(reserva.getDataSaida());
        response.setStatus(reserva.getStatus());
        response.setUsuarioId(reserva.getUsuario().getId());
        response.setNomeUsuario(reserva.getUsuario().getNome());
        response.setQuartoId(reserva.getQuarto().getId());
        response.setNumeroQuarto(reserva.getQuarto().getNumero());
        return response;
    }
}