package com.omar.backend.mymentor.models.dto.mapper;

import java.util.stream.Collectors;

import com.omar.backend.mymentor.models.dto.MentorDto;
import com.omar.backend.mymentor.models.dto.ProfessionalDto;
import com.omar.backend.mymentor.models.entities.Professional;
import com.omar.backend.mymentor.models.entities.UserProfessional;

public class DtoMapperMentorResponse {

    private UserProfessional userProfessional;
    
    private DtoMapperMentorResponse() {
    }

    public static DtoMapperMentorResponse builder() {
        return new DtoMapperMentorResponse();
    }

    public DtoMapperMentorResponse setUserProfessional(UserProfessional userProfessional) {
        this.userProfessional = userProfessional;
        return this;
    }

    public MentorDto build() {
        if (userProfessional == null) {
            throw new RuntimeException("Debe pasar el entity userProfessional!");
        }
        MentorDto mentorDto = new MentorDto();
        mentorDto.setId(userProfessional.getId());
        mentorDto.setProfessional(getProfessionalDto(userProfessional.getProfessional()));
        if(userProfessional.getAdvisorys() != null) {
            mentorDto.setAdvisorys(userProfessional.getAdvisorys()
                    .stream()
                    .map(a -> DtoMapperAdvisoryResponse.builder().setAdvisory(a).build())
                    .collect(Collectors.toList()));
        }
        return mentorDto;
    }

    private ProfessionalDto getProfessionalDto(Professional professional){
        ProfessionalDto professionalDto = new ProfessionalDto();
        professionalDto.setId(professional.getId());
        professionalDto.setName(professional.getName());
        professionalDto.setDescription(professional.getDescription());
        return professionalDto;
    }

}
