package com.example.HealthCare.service;
import java.util.Optional;

import com.example.HealthCare.Util.CustomPagination;
import com.example.HealthCare.dto.request.allergy.AddAllergyRequest;
import com.example.HealthCare.dto.response.AllergyResponse;
import org.springframework.data.domain.Page;
import com.example.HealthCare.model.Allergy;



public interface AllergyService {

    Allergy addAllergy(AddAllergyRequest allergy);

    Allergy updateAllergy(Allergy allergy);

    void deleteAllergy(Integer allergyID);

    CustomPagination<AllergyResponse> getAllAllergies(int page, int size, String keyword, Integer userID, Long memberId);

    Optional <Allergy> findAllergyById(Integer allergyID);
}


