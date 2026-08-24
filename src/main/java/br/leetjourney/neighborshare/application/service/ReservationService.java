package br.leetjourney.neighborshare.application.service;

import br.leetjourney.neighborshare.domain.enums.ItemStatus;
import br.leetjourney.neighborshare.domain.enums.ReservationStatus;
import br.leetjourney.neighborshare.domain.model.Item;
import br.leetjourney.neighborshare.domain.model.Reservation;
import br.leetjourney.neighborshare.domain.model.User;
import br.leetjourney.neighborshare.domain.repository.ItemRepository;
import br.leetjourney.neighborshare.domain.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ItemRepository itemRepository;

    /**
     * Isolation.SERIALIZABLE garante que transações concorrentes na mesma tabela/registro
     * ocorram de forma sequencial, evitando a Race Condition na reserva.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Reservation createReservation(UUID itemId, User requester, LocalDateTime start, LocalDateTime end) {

        if (start.isBefore(LocalDateTime.now()) || end.isBefore(start)) {
            throw new IllegalArgumentException("Datas reservadas invalidas");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (item.getStatus() == ItemStatus.UNAVAILABLE){
            throw new IllegalStateException("Item indisponivel para emprestimo");
        }

        if (item.getOwner().getId().equals(requester.getId())) {
            throw new IllegalStateException("Você não pode reservar seu próprio item.");
        }

        boolean hasConflict = reservationRepository.hasOverlappingReservations(itemId, start, end);
        if (hasConflict) {
            throw new IllegalStateException("Já existe uma reserva aprovada ou ativa para este período.");
        }

        Reservation reservation = Reservation.builder()
                .item(item)
                .requester(requester)
                .startDate(start)
                .endDate(end)
                .status(ReservationStatus.PENDING) // Dono precisa aprovar
                .build();

        return reservationRepository.save(reservation);


    }
}
