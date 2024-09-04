package com.omar.backend.mymentor.models.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa a un mentor en el sistema, que es un profesional asignado a un usuario")
public class MentorDto implements Serializable {

    @JsonProperty("id")
     @Schema(description = "Identificador único del mentor", example = "1")
    private Long id;

    @JsonProperty("advisorys")
    @Schema(description = "Lista de asesorías proporcionadas por el mentor")
    private List<AdvisoryDto> advisorys;

    @JsonProperty("professional")
    @Schema(description = "Información del profesional que actúa como mentor")
    private ProfessionalDto professional;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AdvisoryDto> getAdvisorys() {
        return advisorys;
    }

    public void setAdvisorys(List<AdvisoryDto> advisorys) {
        this.advisorys = advisorys;
    }

    public ProfessionalDto getProfessional() {
        return professional;
    }

    public void setProfessional(ProfessionalDto professional) {
        this.professional = professional;
    }
    
}
