package br.leetjourney.neighborshare.application.dto.response;

import br.leetjourney.neighborshare.domain.enums.CommunityRole;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommunityMemberResponseDTO(
        UUID memberId,
        UUID userId,
        String userName,
        String userEmail,
        CommunityRole role,
        LocalDateTime joinedAt
) {
}
