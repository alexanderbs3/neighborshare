package br.leetjourney.neighborshare.application.service;

import br.leetjourney.neighborshare.application.dto.request.ReviewCreateRequestDTO;
import br.leetjourney.neighborshare.domain.enums.ReservationStatus;
import br.leetjourney.neighborshare.domain.model.Reservation;
import br.leetjourney.neighborshare.domain.model.Review;
import br.leetjourney.neighborshare.domain.repository.ReviewRepository;
import br.leetjourney.neighborshare.domain.model.User;
import br.leetjourney.neighborshare.domain.repository.ReservationRepository;
import br.leetjourney.neighborshare.domain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor

public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createReview(ReviewCreateRequestDTO request, User evaluator) {
        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada."));

        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new IllegalStateException("Apenas reservas concluídas podem receber avaliações.");
        }

        boolean isBorrower = reservation.getRequester().getId().equals(evaluator.getId());
        boolean isOwner = reservation.getItem().getOwner().getId().equals(evaluator.getId());

        if (!isBorrower && !isOwner) {
            throw new IllegalStateException("Apenas o locador ou o locatário desta reserva podem avaliá-la.");
        }

        if (reviewRepository.existsByReservationIdAndEvaluatorId(reservation.getId(), evaluator.getId())) {
            throw new IllegalStateException("Você já enviou uma avaliação para esta reserva.");
        }

        User evaluatedUser = isBorrower ? reservation.getItem().getOwner() : reservation.getRequester();

        Review review = Review.builder()
                .reservation(reservation)
                .evaluator(evaluator)
                .evaluatedUser(evaluatedUser)
                .rating(request.rating())
                .comment(request.comment())
                .build();

        reviewRepository.save(review);
        updateUserReputationScore(evaluatedUser.getId());
    }

    private void updateUserReputationScore(UUID userId) {
        Double averageRating = reviewRepository.calculateAverageRatingForUser(userId).orElse(5.0);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado para atualização de reputação."));

        user.setReputationScore(Math.round(averageRating * 10.0) / 10.0);
        userRepository.save(user);
    }

}
