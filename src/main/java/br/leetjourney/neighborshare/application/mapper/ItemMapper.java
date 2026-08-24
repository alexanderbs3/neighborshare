package br.leetjourney.neighborshare.application.mapper;

import br.leetjourney.neighborshare.application.dto.request.ItemCreateRequestDTO;
import br.leetjourney.neighborshare.application.dto.response.ItemResponseDTO;
import br.leetjourney.neighborshare.domain.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

    Item toEntity(ItemCreateRequestDTO dto);

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.name", target = "ownerName")
    ItemResponseDTO toResponse(Item item);


}
