package com.fernando.hotelreservas.service;


import com.fernando.hotelreservas.dto.QuartoDTO;
import com.fernando.hotelreservas.exception.BusinessException;
import com.fernando.hotelreservas.exception.ResourceNotFoundException;
import com.fernando.hotelreservas.model.Quarto;
import com.fernando.hotelreservas.model.Quarto.StatusQuarto;
import com.fernando.hotelreservas.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuartoService {

    private final QuartoRepository quartoRepository;

    @Transactional
    public QuartoDTO.Response cadastrar(QuartoDTO.Request request) {
        if (quartoRepository.existsByNumero(request.getNumero())) {
            throw new BusinessException("Quarto com número já existe: " + request.getNumero());
        }

        Quarto quarto = Quarto.builder()
                .numero(request.getNumero())
                .tipo(request.getTipo())
                .preco(request.getPreco())
                .status(request.getStatus())
                .hotelId(request.getHotelId())
                .build();

        return toResponse(quartoRepository.save(quarto));
    }

    public List<QuartoDTO.Response> listarTodos() {
        return quartoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public QuartoDTO.Response buscarPorId(Long id) {
        return toResponse(findById(id));
    }

    public List<QuartoDTO.Response> listarDisponiveis() {
        return quartoRepository.findByStatus(StatusQuarto.DISPONIVEL)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<QuartoDTO.Response> listarDisponiveisPorPeriodo(LocalDate dataEntrada, LocalDate dataSaida) {
        if (!dataSaida.isAfter(dataEntrada)) {
            throw new BusinessException("Data de saída deve ser após a data de entrada");
        }
        return quartoRepository.findQuartosDisponiveisPorPeriodo(dataEntrada, dataSaida)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public QuartoDTO.Response atualizar(Long id, QuartoDTO.UpdateRequest request) {
        Quarto quarto = findById(id);

        if (request.getNumero() != null) {
            if (!request.getNumero().equals(quarto.getNumero())
                    && quartoRepository.existsByNumero(request.getNumero())) {
                throw new BusinessException("Quarto com número já existe: " + request.getNumero());
            }
            quarto.setNumero(request.getNumero());
        }
        if (request.getTipo() != null) quarto.setTipo(request.getTipo());
        if (request.getPreco() != null) quarto.setPreco(request.getPreco());
        if (request.getStatus() != null) quarto.setStatus(request.getStatus());
        if (request.getHotelId() != null) quarto.setHotelId(request.getHotelId());

        return toResponse(quartoRepository.save(quarto));
    }

    @Transactional
    public void excluir(Long id) {
        if (!quartoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Quarto não encontrado com id: " + id);
        }
        quartoRepository.deleteById(id);
    }

    public Quarto findById(Long id) {
        return quartoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quarto não encontrado com id: " + id));
    }

    public QuartoDTO.Response toResponse(Quarto quarto) {
        QuartoDTO.Response response = new QuartoDTO.Response();
        response.setId(quarto.getId());
        response.setNumero(quarto.getNumero());
        response.setTipo(quarto.getTipo());
        response.setPreco(quarto.getPreco());
        response.setStatus(quarto.getStatus());
        response.setHotelId(quarto.getHotelId());
        return response;
    }
}