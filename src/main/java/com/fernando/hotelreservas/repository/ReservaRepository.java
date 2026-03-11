package com.fernando.hotelreservas.repository;


import com.fernando.hotelreservas.model.Reserva;
import com.fernando.hotelreservas.model.Reserva.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioId(Long usuarioId);

    List<Reserva> findByStatus(StatusReserva status);

    @Query("""
        SELECT COUNT(r) > 0 FROM Reserva r
        WHERE r.quarto.id = :quartoId
        AND r.status NOT IN ('CANCELADA')
        AND r.dataEntrada < :dataSaida
        AND r.dataSaida > :dataEntrada
    """)
    boolean existsConflito(
            @Param("quartoId") Long quartoId,
            @Param("dataEntrada") LocalDate dataEntrada,
            @Param("dataSaida") LocalDate dataSaida
    );
}