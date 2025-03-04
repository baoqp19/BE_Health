package com.example.HealthCare.service;
import java.util.Optional;
import org.springframework.data.domain.Page;
import com.example.HealthCare.model.Allergy;



public interface AllergyService {

    public Allergy addAllergy(Allergy allergy);

    Allergy updateAllergy(Allergy allergy);

    void deleteAllergy(Integer allergyID); 

    Page<Allergy> getAllAllergies(int page, int size, String keyword);

    // Search
    Optional<Allergy> findAllergyById(Integer allergyID);
    // Page<Allergy> findBySeverity(String severity,Pageable pageable);
    // Page<Allergy> findByAllergyType(String allergyType, Pageable pageable);
}


