package br.leetjourney.neighborshare.application.dto.response;

public record AuthResponseDTO(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn
) {
    public AuthResponseDTO(String accessToken, String refreshToken, long expiresIn) {
        this(accessToken, refreshToken, "Bearer", expiresIn);
    }

}
