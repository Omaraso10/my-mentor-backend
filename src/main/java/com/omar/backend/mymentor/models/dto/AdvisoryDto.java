package com.omar.backend.mymentor.models.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa una asesoría completa, incluyendo detalles de la conversación y metadatos")
public class AdvisoryDto implements Serializable{

    @JsonProperty("id")
    @Schema(example = "1", description = "ID único de la asesoría")
    private Long id;

    @JsonProperty("description")
    @Schema(example = "Asesoría sobre desarrollo de software", 
            description = "Descripción breve de la asesoría")
    private String description;

    @JsonProperty("advisorys_details")
    @Schema(description = "Lista de detalles de la asesoría, incluyendo preguntas, respuestas e imágenes en base64")
    private List<AdvisoryDetailDto> advisorysDetails;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AdvisoryDetailDto> getAdvisorysDetails() {
        return advisorysDetails;
    }

    public void setAdvisorysDetails(List<AdvisoryDetailDto> advisorysDetails) {
        this.advisorysDetails = advisorysDetails;
    }

    
}
