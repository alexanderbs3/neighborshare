package br.leetjourney.neighborshare.application.mapper;

import br.leetjourney.neighborshare.application.dto.request.CommunityCreateRequestDTO;
import br.leetjourney.neighborshare.application.dto.response.CommunityResponseDTO;
import br.leetjourney.neighborshare.domain.model.Community;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommunityMapper {

    Community toEntity(CommunityCreateRequestDTO dto);

    @Mapping(target = "memberCount", ignore = true)
    @Mapping(source = "createdDate", target = "createdAt")
    CommunityResponseDTO toResponse(Community community);
}
