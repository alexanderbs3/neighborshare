package br.leetjourney.neighborshare.application.mapper;

import br.leetjourney.neighborshare.application.dto.response.CommunityMemberResponseDTO;
import br.leetjourney.neighborshare.domain.model.CommunityMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommunityMemberMapper {

    @Mapping(source = "id", target = "memberId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "createdDate", target = "joinedAt")
    CommunityMemberResponseDTO toResponse(CommunityMember member);
}
