package com.example.HealthCare.mapper;

import com.example.HealthCare.dto.response.VaccicationResponse;
import com.example.HealthCare.model.Vaccication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface VaccicationMapper {
    @Mapping(source = "member.memberID", target = "member.memberID")
    VaccicationResponse toVaccicationResponse(Vaccication vaccication);

    default Page<VaccicationResponse> toVaccicationsResponse(Page<Vaccication> vaccications) {
        return vaccications.map(this::toVaccicationResponse);
    }
}
