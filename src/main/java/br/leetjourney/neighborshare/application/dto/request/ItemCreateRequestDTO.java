package br.leetjourney.neighborshare.application.dto.request;

import br.leetjourney.neighborshare.domain.enums.ItemCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record ItemCreateRequestDTO(
        @NotBlank(message = "O nome é obrigatorio")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracters")
        String name,

        @NotBlank(message = "A categoria é obrigatoria")
        String category,

        @NotNull(message = "A condição do item é obrigatoria")
        ItemCondition condition,

        @NotNull(message = "O ID da comunidade  é obrigatoria")
        UUID commumityId,

        String loadRules,

        List<String> photoUrls
) {}
