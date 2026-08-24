package br.leetjourney.neighborshare.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommunityCreateRequestDTO(
        @NotBlank(message = "O nome da comunidade é obrigatório.")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String name,

        @Size(max = 500, message = "A descrição pode ter no máximo 500 caracteres.")
        String description
) {
}
