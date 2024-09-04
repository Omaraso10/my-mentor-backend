package com.omar.backend.mymentor.models.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public class AskProfessionalDto implements Serializable{

    @JsonProperty("user_professional_id")
    @Schema(example = "1", description = "ID de la relación entre el profesional y el usuario")
    private Long userProfessionalId;

    @NotEmpty(message = "no puede estar vacio")
    @JsonProperty("ask")
    @Schema(example = "¿Cuáles son las mejores prácticas para el desarrollo de software?", 
            description = "Pregunta para el profesional")
    private String ask;

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public Long getUserProfessionalId() {
        return userProfessionalId;
    }

    public void setUserProfessionalId(Long userProfessionalId) {
        this.userProfessionalId = userProfessionalId;
    }

}
