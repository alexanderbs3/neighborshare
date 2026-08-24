package br.leetjourney.neighborshare.application.dto.request;

import br.leetjourney.neighborshare.domain.enums.CommunityRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequestDTO(
        @NotNull(message = "O papel (role) do membro é obrigatório.")
        CommunityRole role
) {
}
