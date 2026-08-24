package br.leetjourney.neighborshare.api.controller;


import br.leetjourney.neighborshare.application.dto.request.ReviewCreateRequestDTO;
import br.leetjourney.neighborshare.application.service.ReviewService;
import br.leetjourney.neighborshare.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reservas", description = "Ciclo de vida e gestão de agendamentos de empréstimo")
public class ReservationController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    @Operation(summary = "Avaliar a contraparte de uma reserva concluída")
    public ResponseEntity<Void> createReview(
            @Valid @RequestBody ReviewCreateRequestDTO request,
            @AuthenticationPrincipal User evaluator
    ) {
        reviewService.createReview(request, evaluator);
        return ResponseEntity.noContent().build();
    }
}
