package br.leetjourney.neighborshare.api.controller;


import br.leetjourney.neighborshare.application.dto.request.UpdateMemberRoleRequestDTO;
import br.leetjourney.neighborshare.application.dto.response.CommunityMemberResponseDTO;
import br.leetjourney.neighborshare.application.service.CommunityMemberService;
import br.leetjourney.neighborshare.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/communities/{communityId}/members")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Membros da Comunidade", description = "Endpoints para listagem, alteração de cargos e remoção de membros")
public class CommunityMemberController {

    private final CommunityMemberService memberService;

    @GetMapping
    @Operation(summary = "Listar membros da comunidade de forma paginada")
    public ResponseEntity<Page<CommunityMemberResponseDTO>> listMembers(
            @PathVariable UUID communityId,
            @AuthenticationPrincipal User currentUser,
            Pageable pageable
    ) {
        return ResponseEntity.ok(memberService.listMembers(communityId, currentUser, pageable));
    }

    @PatchMapping("/{memberId}/role")
    @Operation(summary = "Alterar o papel de um membro (Exige privilégio de COMMUNITY_ADMIN)")
    public ResponseEntity<CommunityMemberResponseDTO> updateMemberRole(
            @PathVariable UUID communityId,
            @PathVariable UUID memberId,
            @Valid @RequestBody UpdateMemberRoleRequestDTO request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(memberService.updateMemberRole(communityId, memberId, request, currentUser));
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "Remover/expulsar um membro da comunidade (Exige privilégio de COMMUNITY_ADMIN)")
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID communityId,
            @PathVariable UUID memberId,
            @AuthenticationPrincipal User currentUser
    ) {
        memberService.removeMember(communityId, memberId, currentUser);
        return ResponseEntity.noContent().build();
    }
}
