package br.leetjourney.neighborshare.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ReviewCreateRequestDTO(
        @NotNull(message = "O ID da reserva é obrigatório.")
        UUID reservationId,

        @NotNull(message = "A nota é obrigatória.")
        @Min(value = 1, message = "A nota mínima é 1.")
        @Max(value = 5, message = "A nota máxima é 5.")
        Integer rating,

        @Size(max = 1000, message = "O comentário pode ter no máximo 1000 caracteres.")
        String comment
) {
}
