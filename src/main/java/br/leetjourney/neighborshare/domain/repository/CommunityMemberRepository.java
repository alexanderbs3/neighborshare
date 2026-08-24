package br.leetjourney.neighborshare.domain.repository;

import br.leetjourney.neighborshare.domain.model.CommunityMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CommunityMemberRepository extends JpaRepository<CommunityMember, UUID> {

    @Query(
            value = "SELECT cm FROM CommunityMember cm JOIN FETCH cm.user WHERE cm.community.id = :communityId",
            countQuery = "SELECT COUNT(cm) FROM CommunityMember cm WHERE cm.community.id = :communityId"
    )
    Page<CommunityMember> findAllByCommunityIdWithUser(@Param("communityId") UUID communityId, Pageable pageable);

    @Query("SELECT cm FROM CommunityMember cm JOIN FETCH cm.user WHERE cm.id = :memberId AND cm.community.id = :communityId")
    Optional<CommunityMember> findByIdAndCommunityId(@Param("memberId") UUID memberId, @Param("communityId") UUID communityId);

    boolean existsByCommunityIdAndUserId(UUID communityId, UUID userId);

    Optional<CommunityMember> findByCommunityIdAndUserId(UUID communityId, UUID userId);

    long countByCommunityId(UUID communityId);

    long countByCommunityIdAndRole(UUID communityId, br.leetjourney.neighborshare.domain.enums.CommunityRole role);

    @Query(
            value = "SELECT cm FROM CommunityMember cm JOIN FETCH cm.community WHERE cm.user.id = :userId",
            countQuery = "SELECT COUNT(cm) FROM CommunityMember cm WHERE cm.user.id = :userId"
    )
    Page<CommunityMember> findAllByUserIdWithCommunity(@Param("userId") UUID userId, Pageable pageable);
}
