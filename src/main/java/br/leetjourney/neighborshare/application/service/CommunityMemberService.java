package br.leetjourney.neighborshare.application.service;

import br.leetjourney.neighborshare.application.dto.request.UpdateMemberRoleRequestDTO;
import br.leetjourney.neighborshare.application.dto.response.CommunityMemberResponseDTO;
import br.leetjourney.neighborshare.application.mapper.CommunityMemberMapper;
import br.leetjourney.neighborshare.domain.enums.CommunityRole;
import br.leetjourney.neighborshare.domain.enums.GlobalRole;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityMemberService {


    private final CommunityMemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final CommunityMemberMapper memberMapper;

    @Transactional(readOnly = true)
    public Page<CommunityMemberResponseDTO> listMembers(UUID communityId, User requester, Pageable pageable) {
        validateCommunityExistence(communityId);
        validateRequesterMembership(communityId, requester);

        return memberRepository.findAllByCommunityIdWithUser(communityId, pageable)
                .map(memberMapper::toResponse);
    }

    @Transactional
    public CommunityMemberResponseDTO updateMemberRole(
            UUID communityId,
            UUID memberId,
            UpdateMemberRoleRequestDTO request,
            User requester
    ) {
        validateCommunityExistence(communityId);
        validateRequesterIsAdmin(communityId, requester);

        CommunityMember targetMember = memberRepository.findByIdAndCommunityId(memberId, communityId)
                .orElseThrow(() -> new EntityNotFoundException("Membro não encontrado nesta comunidade."));

        // Regra de segurança: impede rebaixar o único admin da comunidade
        if (targetMember.getRole() == CommunityRole.COMMUNITY_ADMIN
                && request.role() == CommunityRole.MEMBER) {
            long adminCount = memberRepository.countByCommunityIdAndRole(communityId, CommunityRole.COMMUNITY_ADMIN);
            if (adminCount <= 1) {
                throw new IllegalStateException("Não é possível rebaixar o único administrador da comunidade.");
            }
        }

        targetMember.setRole(request.role());
        return memberMapper.toResponse(memberRepository.save(targetMember));
    }

    @Transactional
    public void removeMember(UUID communityId, UUID memberId, User requester) {
        validateCommunityExistence(communityId);
        validateRequesterIsAdmin(communityId, requester);

        CommunityMember targetMember = memberRepository.findByIdAndCommunityId(memberId, communityId)
                .orElseThrow(() -> new EntityNotFoundException("Membro não encontrado nesta comunidade."));

        // Impede expulsar a si próprio por essa rota (deve usar /leave)
        if (targetMember.getUser().getId().equals(requester.getId())) {
            throw new IllegalStateException("Para sair da comunidade, utilize a opção de desligamento voluntário.");
        }

        if (targetMember.getRole() == CommunityRole.COMMUNITY_ADMIN) {
            long adminCount = memberRepository.countByCommunityIdAndRole(communityId, CommunityRole.COMMUNITY_ADMIN);
            if (adminCount <= 1) {
                throw new IllegalStateException("Não é possível remover o único administrador da comunidade.");
            }
        }

        memberRepository.delete(targetMember);
    }

    private void validateCommunityExistence(UUID communityId) {
        if (!communityRepository.existsById(communityId)) {
            throw new EntityNotFoundException("Comunidade não encontrada.");
        }
    }

    private void validateRequesterMembership(UUID communityId, User requester) {
        if (requester.getGlobalRole() == GlobalRole.ADMIN) return;

        boolean isMember = memberRepository.existsByCommunityIdAndUserId(communityId, requester.getId());
        if (!isMember) {
            throw new IllegalStateException("Você precisa pertencer a esta comunidade para visualizar seus membros.");
        }
    }

    private void validateRequesterIsAdmin(UUID communityId, User requester) {
        if (requester.getGlobalRole() == GlobalRole.ADMIN) return;

        CommunityMember requesterMember = memberRepository.findByCommunityIdAndUserId(communityId, requester.getId())
                .orElseThrow(() -> new IllegalStateException("Você não pertence a esta comunidade."));

        if (requesterMember.getRole() != CommunityRole.COMMUNITY_ADMIN) {
            throw new IllegalStateException("Apenas administradores da comunidade podem realizar esta ação.");
        }
    }
}
