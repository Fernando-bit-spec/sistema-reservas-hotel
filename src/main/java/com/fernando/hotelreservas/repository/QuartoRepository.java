package com.fernando.hotelreservas.repository;


import com.fernando.hotelreservas.model.Quarto;
import com.fernando.hotelreservas.model.Quarto.StatusQuarto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuartoRepository extends JpaRepository<Quarto, Long> {

    List<Quarto> findByStatus(StatusQuarto status);

    boolean existsByNumero(String numero);

    @Query("""
        SELECT q FROM Quarto q
        WHERE q.status = 'DISPONIVEL'
        AND q.id NOT IN (
            SELECT r.quarto.id FROM Reserva r
            WHERE r.status NOT IN ('CANCELADA')
            AND r.dataEntrada < :dataSaida
            AND r.dataSaida > :dataEntrada
        )
    """)
    List<Quarto> findQuartosDisponiveisPorPeriodo(
            @Param("dataEntrada") LocalDate dataEntrada,
            @Param("dataSaida") LocalDate dataSaida
    );
}




