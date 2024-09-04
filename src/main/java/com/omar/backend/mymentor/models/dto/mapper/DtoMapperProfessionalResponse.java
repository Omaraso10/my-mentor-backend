package com.omar.backend.mymentor.models.dto.mapper;

import org.springframework.stereotype.Component;

import com.omar.backend.mymentor.models.dto.ProfessionalDto;
import com.omar.backend.mymentor.models.entities.Professional;

@Component
public class DtoMapperProfessionalResponse {

    private Professional professional;
    
    private DtoMapperProfessionalResponse() {
    }

    public static DtoMapperProfessionalResponse builder() {
        return new DtoMapperProfessionalResponse();
    }

    public DtoMapperProfessionalResponse setProfessional(Professional professional) {
        this.professional = professional;
        return this;
    }

    public ProfessionalDto build() {
        if (professional == null) {
            throw new RuntimeException("Debe pasar el entity professional!");
        }
        ProfessionalDto professionalDto = new ProfessionalDto();
        professionalDto.setId(professional.getId());
        professionalDto.setName(professional.getName());
        professionalDto.setDescription(professional.getDescription());
        return professionalDto;
    }

}
