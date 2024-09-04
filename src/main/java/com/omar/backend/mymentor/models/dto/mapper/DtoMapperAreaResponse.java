package com.omar.backend.mymentor.models.dto.mapper;

import org.springframework.stereotype.Component;

import com.omar.backend.mymentor.models.dto.AreaDto;
import com.omar.backend.mymentor.models.entities.Area;

@Component
public class DtoMapperAreaResponse {

    private Area area;
    
    private DtoMapperAreaResponse() {
    }

    public static DtoMapperAreaResponse builder() {
        return new DtoMapperAreaResponse();
    }

    public DtoMapperAreaResponse setArea(Area area) {
        this.area = area;
        return this;
    }

    public AreaDto build() {
        if (area == null) {
            throw new RuntimeException("Debe pasar el entity area!");
        }
        AreaDto areaDto = new AreaDto();
        areaDto.setId(area.getId());
        areaDto.setName(area.getName());
        return areaDto;
    }

}
