package com.omar.backend.mymentor.models.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.omar.backend.mymentor.models.dto.MentorDto;
import com.omar.backend.mymentor.models.dto.ProfessionalDto;
import com.omar.backend.mymentor.models.dto.UserResponseDTO;
import com.omar.backend.mymentor.models.entities.Professional;
import com.omar.backend.mymentor.models.entities.User;
import com.omar.backend.mymentor.models.entities.UserProfessional;

@Component
public class DtoMapperUserResponse {

    private User user;
    
    private DtoMapperUserResponse() {
    }

    public static DtoMapperUserResponse builder() {
        return new DtoMapperUserResponse();
    }

    public DtoMapperUserResponse setUser(User user) {
        this.user = user;
        return this;
    }

    public UserResponseDTO build() {
        if (user == null) {
            throw new RuntimeException("Debe pasar el entity user!");
        }
        boolean isAdmin = user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getName()));
        UserResponseDTO userResponseDto = new UserResponseDTO();
        userResponseDto.setUuid(user.getUuid());
        userResponseDto.setName(user.getName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setPhoneNumber(user.getPhoneNumber());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setAdmin(isAdmin);
        userResponseDto.setEnabled(user.getEnabled());
        List<UserProfessional> mentors = user.getMentors();
        userResponseDto.setMentors(mentors != null ? getMentors(mentors) : null);
        return userResponseDto;
    }

    private List<MentorDto> getMentors(List<UserProfessional> userProfessionals){
        return userProfessionals.stream()
                .map(this::getMentor)
                .collect(Collectors.toList());
    }

    private MentorDto getMentor(UserProfessional mentor){
        MentorDto mentorDto = new MentorDto();
        mentorDto.setId(mentor.getId());
        mentorDto.setProfessional(getProfessionalDto(mentor.getProfessional()));
        if(mentor.getAdvisorys() != null) {
            mentorDto.setAdvisorys(mentor.getAdvisorys()
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
