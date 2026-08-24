package br.leetjourney.neighborshare.domain.repository;

import br.leetjourney.neighborshare.application.dto.response.ItemResponseDTO;
import br.leetjourney.neighborshare.domain.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommunityRepository extends JpaRepository<Community, UUID> {

    Optional<Community> findByInviteCode(String inviteCode);
    boolean existsByInviteCode(String inviteCode);
}
