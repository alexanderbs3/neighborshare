package br.leetjourney.neighborshare.domain.repository;

import br.leetjourney.neighborshare.domain.enums.ItemStatus;
import br.leetjourney.neighborshare.domain.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ItemRepository extends JpaRepository<Item, UUID> {
    Page<Item> findByCommunityIdAndStatus(UUID communityId, ItemStatus status, Pageable pageable);

    @Query("SELECT i FROM Item i JOIN FETCH i.owner WHERE i.id = :id")
    Item findByIdWithOwner(@Param("id") UUID id);
}
