package br.leetjourney.neighborshare.application.service;

import br.leetjourney.neighborshare.application.dto.request.CommunityCreateRequestDTO;
import br.leetjourney.neighborshare.application.dto.response.CommunityResponseDTO;
import br.leetjourney.neighborshare.application.mapper.CommunityMapper;
import br.leetjourney.neighborshare.domain.enums.CommunityRole;
import br.leetjourney.neighborshare.domain.model.Community;
import br.leetjourney.neighborshare.domain.model.CommunityMember;
import br.leetjourney.neighborshare.domain.model.User;
import br.leetjourney.neighborshare.domain.repository.CommunityMemberRepository;
import br.leetjourney.neighborshare.domain.repository.CommunityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final CommunityMemberRepository memberRepository;
    private final CommunityMapper communityMapper;

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public CommunityResponseDTO createCommunity(CommunityCreateRequestDTO dto, User creator) {
        Community community = communityMapper.toEntity(dto);
        community.setInviteCode(generateUniqueInviteCode());

        Community savedCommunity = communityRepository.save(community);

        CommunityMember adminMember = CommunityMember.builder()
                .community(savedCommunity)
                .user(creator)
                .role(CommunityRole.COMMUNITY_ADMIN)
                .build();

        memberRepository.save(adminMember);

        return buildResponseDTO(savedCommunity, 1L);
    }

    @Transactional
    public CommunityResponseDTO joinByInviteCode(String inviteCode, User user) {
        Community community = communityRepository.findByInviteCode(inviteCode.toUpperCase().trim())
                .orElseThrow(() -> new EntityNotFoundException("Comunidade não encontrada para o código de convite informado."));

        if (memberRepository.existsByCommunityIdAndUserId(community.getId(), user.getId())) {
            throw new IllegalStateException("O usuário já faz parte desta comunidade.");
        }

        CommunityMember newMember = CommunityMember.builder()
                .community(community)
                .user(user)
                .role(CommunityRole.MEMBER)
                .build();

        memberRepository.save(newMember);

        long count = memberRepository.countByCommunityId(community.getId());
        return buildResponseDTO(community, count);
    }

    @Transactional
    public void leaveCommunity(UUID communityId, User user) {
        CommunityMember member = memberRepository.findByCommunityIdAndUserId(communityId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Membro não encontrado nesta comunidade."));

        if (member.getRole() == CommunityRole.COMMUNITY_ADMIN) {
            long adminCount = memberRepository.countByCommunityIdAndRole(communityId, CommunityRole.COMMUNITY_ADMIN);
            long totalMembers = memberRepository.countByCommunityId(communityId);

            if (adminCount == 1 && totalMembers > 1) {
                throw new IllegalStateException("Promova outro membro a COMMUNITY_ADMIN antes de sair da comunidade.");
            }
        }

        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public CommunityResponseDTO getDetails(UUID communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new EntityNotFoundException("Comunidade não encontrada."));

        long memberCount = memberRepository.countByCommunityId(communityId);
        return buildResponseDTO(community, memberCount);
    }

    @Transactional(readOnly = true)
    public Page<CommunityResponseDTO> listUserCommunities(User user, Pageable pageable) {
        return memberRepository.findAllByUserIdWithCommunity(user.getId(), pageable)
                .map(cm -> {
                    long count = memberRepository.countByCommunityId(cm.getCommunity().getId());
                    return buildResponseDTO(cm.getCommunity(), count);
                });
    }

    private String generateUniqueInviteCode() {
        String code;
        do {
            StringBuilder sb = new StringBuilder(8);
            for (int i = 0; i < 8; i++) {
                sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
            }
            code = sb.toString();
        } while (communityRepository.existsByInviteCode(code));
        return code;
    }

    private CommunityResponseDTO buildResponseDTO(Community community, long count) {
        return new CommunityResponseDTO(
                community.getId(),
                community.getName(),
                community.getDescription(),
                community.getInviteCode(),
                count,
                community.getCreatedDate()
        );
    }
}