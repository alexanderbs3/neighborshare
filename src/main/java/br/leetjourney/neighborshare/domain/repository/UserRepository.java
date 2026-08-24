package br.leetjourney.neighborshare.domain.repository;

import br.leetjourney.neighborshare.domain.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(@NotBlank(message = "O e-mail é obrigatório") @Email(message = "Formato de e-mail inválido") String email);

    Optional<User> findByEmail(String email);
}
