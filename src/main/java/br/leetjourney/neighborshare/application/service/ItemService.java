package br.leetjourney.neighborshare.application.service;

import br.leetjourney.neighborshare.application.dto.request.ItemCreateRequestDTO;
import br.leetjourney.neighborshare.domain.enums.ItemStatus;
import br.leetjourney.neighborshare.domain.model.Community;
import br.leetjourney.neighborshare.domain.model.Item;
import br.leetjourney.neighborshare.domain.model.User;
import br.leetjourney.neighborshare.domain.repository.CommunityRepository;
import br.leetjourney.neighborshare.domain.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final CommunityRepository communityRepository;


    @Transactional
    public Item createItem(ItemCreateRequestDTO dto, User owner) {
        Community community = communityRepository.findById(dto.communityId())
                .orElseThrow(() -> new EntityNotFoundException("Comunidade não encontrada com o ID informado."));

        Item item = Item.builder()
                .name(dto.name())
                .category(dto.category())
                .condition(dto.condition())
                .status(ItemStatus.AVAILABLE)
                .owner(owner)
                .community(community)
                .loanRules(dto.loanRules())
                .photoUrls(dto.photoUrls())
                .build();

        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public Page<Item> listByCommunity(UUID communityId, ItemStatus status, Pageable pageable) {
        if (!communityRepository.existsById(communityId)) {
            throw new EntityNotFoundException("Comunidade não encontrada.");
        }
        return itemRepository.findByCommunityIdAndStatus(communityId, status, pageable);
    }

    @Transactional(readOnly = true)
    public Item findById(UUID itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado."));
    }
}
