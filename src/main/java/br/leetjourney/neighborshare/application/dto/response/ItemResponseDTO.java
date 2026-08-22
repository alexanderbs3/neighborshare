package br.leetjourney.neighborshare.application.dto.response;

import br.leetjourney.neighborshare.domain.enums.ItemCondition;
import br.leetjourney.neighborshare.domain.enums.ItemStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ItemResponseDTO(
        UUID id,
        String name,
        String category,
        ItemCondition condition,
        ItemStatus status,
        String loanRules,
        List<String> photoUrls,
        UUID ownerId,
        String ownerName,
        LocalDateTime createdAt
) {
}
