package br.leetjourney.neighborshare.domain.repository;

import br.leetjourney.neighborshare.domain.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    boolean existsByReservationIdAndEvaluatorId(UUID reservationId, UUID evaluatorId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.evaluatedUser.id = :userId")
    Optional<Double> calculateAverageRatingForUser(@Param("userId") UUID userId);
}
