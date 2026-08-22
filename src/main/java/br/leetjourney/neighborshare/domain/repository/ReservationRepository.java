package br.leetjourney.neighborshare.domain.repository;

import br.leetjourney.neighborshare.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {


    @Query("""
            SELECT COUNT(r) > 0 FROM Reservation r
            WHERE r.item.id = :itemId
            AND r.status IN ('APPROVED', 'ACTIVE')
            AND (r.startDate <= :endDate AND r.endDate >= :startDate)
            """)
    boolean hasOverlappingReservations(
            @Param("itemId") UUID itemId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
