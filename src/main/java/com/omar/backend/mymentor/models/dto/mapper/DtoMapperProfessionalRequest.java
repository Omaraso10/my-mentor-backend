package com.omar.backend.mymentor.models.dto.mapper;

import org.springframework.stereotype.Component;

import com.omar.backend.mymentor.models.dto.AreaDto;
import com.omar.backend.mymentor.models.dto.ProfessionalDto;
import com.omar.backend.mymentor.models.entities.Area;
import com.omar.backend.mymentor.models.entities.Professional;

@Component
public class DtoMapperProfessionalRequest {

    private ProfessionalDto professionalDto;

    public static DtoMapperProfessionalRequest builder() {
        return new DtoMapperProfessionalRequest();
    }

    public DtoMapperProfessionalRequest setProfessional(ProfessionalDto professionalDto) {
        this.professionalDto = professionalDto;
        return this;
    }

    public Professional build() {
        if (professionalDto == null) {
            throw new RuntimeException("Debe pasar el entity professional!");
        }
        Professional professional = new Professional();
        professional.setName(professionalDto.getName());
        professional.setDescription(professionalDto.getDescription());
        professional.setArea(getArea(professionalDto.getArea()));
        return professional;
    }

    public Area getArea(AreaDto areaDto) {
        Area area = new Area();
        area.setId(areaDto.getId());
        area.setName(areaDto.getName());
        return area;
    }
    
}
