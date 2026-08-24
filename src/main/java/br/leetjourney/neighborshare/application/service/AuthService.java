package br.leetjourney.neighborshare.application.service;

import br.leetjourney.neighborshare.application.dto.request.AuthRequestDTO;
import br.leetjourney.neighborshare.application.dto.request.RegisterRequestDTO;
import br.leetjourney.neighborshare.application.dto.response.AuthResponseDTO;
import br.leetjourney.neighborshare.domain.enums.GlobalRole;
import br.leetjourney.neighborshare.domain.model.User;
import br.leetjourney.neighborshare.domain.repository.UserRepository;
import br.leetjourney.neighborshare.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalStateException("E-mail já cadastrado na plataforma.");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .globalRole(GlobalRole.USER)
                .reputationScore(5.0)
                .build();

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponseDTO(accessToken, refreshToken, jwtExpiration / 1000);
    }

    @Transactional(readOnly = true)
    public AuthResponseDTO login(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponseDTO(accessToken, refreshToken, jwtExpiration / 1000);
    }
}
