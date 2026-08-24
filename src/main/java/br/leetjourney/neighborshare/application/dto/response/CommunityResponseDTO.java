package br.leetjourney.neighborshare.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommunityResponseDTO(
        UUID id,
        String name,
        String description,
        String inviteCode,
        long memberCount,
        LocalDateTime createdAt
) {
}
