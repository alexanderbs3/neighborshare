package br.leetjourney.neighborshare.api.controller;

import br.leetjourney.neighborshare.application.service.FileStorageService;
import br.leetjourney.neighborshare.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Mídias & Storage", description = "Geração de URLs pré-assinadas para upload seguro de fotos no S3")
public class MediaController {

    private final FileStorageService fileStorageService;

    @PostMapping("/presigned-url")
    @Operation(summary = "Gerar Presigned URL para upload direto de foto de item")
    public ResponseEntity<FileStorageService.PresignedUploadResponse> generatePresignedUrl(
            @RequestParam String filename,
            @RequestParam String contentType,
            @AuthenticationPrincipal User currentUser
    ) {
        FileStorageService.PresignedUploadResponse response = fileStorageService.generatePresignedUploadUrl(
                filename, contentType, currentUser.getId()
        );
        return ResponseEntity.ok(response);
    }
}
