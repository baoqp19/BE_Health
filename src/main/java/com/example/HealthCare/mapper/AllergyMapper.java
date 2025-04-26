package com.example.HealthCare.mapper;


import com.example.HealthCare.dto.request.allergy.AddAllergyRequest;
import com.example.HealthCare.dto.response.AllergyResponse;
import com.example.HealthCare.model.Allergy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface AllergyMapper {
    AllergyMapper INSTANCE = Mappers.getMapper( AllergyMapper.class );
    Allergy convectToAllergy(AddAllergyRequest allergy);
    AllergyResponse toAllergyResponse(Allergy allergy);

    default Page<AllergyResponse> toAllergiesResponse(Page<Allergy> allergies) {
        return allergies.map(this::toAllergyResponse);
    }
}