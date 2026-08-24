package br.leetjourney.neighborshare.api.controller;

import br.leetjourney.neighborshare.application.dto.request.CommunityCreateRequestDTO;
import br.leetjourney.neighborshare.application.dto.response.CommunityResponseDTO;
import br.leetjourney.neighborshare.application.service.CommunityService;
import br.leetjourney.neighborshare.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/communities")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Comunidades", description = "Endpoints para criação, gestão e ingresso em comunidades por código de convite")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping
    @Operation(summary = "Criar uma nova comunidade (O criador se torna COMMUNITY_ADMIN)")
    public ResponseEntity<CommunityResponseDTO> create(
            @Valid @RequestBody CommunityCreateRequestDTO request,
            @AuthenticationPrincipal User currentUser
    ) {
        CommunityResponseDTO response = communityService.createCommunity(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/join")
    @Operation(summary = "Entrar em uma comunidade utilizando código de convite")
    public ResponseEntity<CommunityResponseDTO> joinByInviteCode(
            @RequestParam String inviteCode,
            @AuthenticationPrincipal User currentUser
    ) {
        CommunityResponseDTO response = communityService.joinByInviteCode(inviteCode, currentUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{communityId}/leave")
    @Operation(summary = "Sair de uma comunidade")
    public ResponseEntity<Void> leave(
            @PathVariable UUID communityId,
            @AuthenticationPrincipal User currentUser
    ) {
        communityService.leaveCommunity(communityId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{communityId}")
    @Operation(summary = "Obter detalhes de uma comunidade específica")
    public ResponseEntity<CommunityResponseDTO> getDetails(@PathVariable UUID communityId) {
        return ResponseEntity.ok(communityService.getDetails(communityId));
    }

    @GetMapping("/my")
    @Operation(summary = "Listar comunidades que o usuário autenticado participa")
    public ResponseEntity<Page<CommunityResponseDTO>> listMyCommunities(
            @AuthenticationPrincipal User currentUser,
            Pageable pageable
    ) {
        return ResponseEntity.ok(communityService.listUserCommunities(currentUser, pageable));
    }
}


