package br.leetjourney.neighborshare.api.controller;


import br.leetjourney.neighborshare.application.dto.request.ItemCreateRequestDTO;
import br.leetjourney.neighborshare.application.dto.response.ItemResponseDTO;
import br.leetjourney.neighborshare.application.mapper.ItemMapper;
import br.leetjourney.neighborshare.application.service.ItemService;
import br.leetjourney.neighborshare.domain.enums.ItemStatus;
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
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Itens", description = "Gerenciamento do catálogo de itens para empréstimo")
public class ItemController {

    private final ItemService itemService;
    private ItemMapper itemMapper;

    @PostMapping
    @Operation(summary = "Cadastrar novo item")
    public ResponseEntity<ItemResponseDTO> create(
            @Valid @RequestBody ItemCreateRequestDTO dto,
            @AuthenticationPrincipal User currentUser
    ) {
        var createdItem = itemService.createItem(dto, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemMapper.toResponse(createdItem));
    }

    @GetMapping("/community/{communityId}")
    @Operation(summary = "Listar itens por comunidade de forma paginada")
    public ResponseEntity<Page<ItemResponseDTO>> listByCommunity(
            @PathVariable UUID communityId,
            @RequestParam(defaultValue = "AVAILABLE") ItemStatus status,
            Pageable pageable
    ) {
        Page<ItemResponseDTO> items = itemService.listByCommunity(communityId, status, pageable)
                .map(itemMapper::toResponse);
        return ResponseEntity.ok(items);
    }
}
