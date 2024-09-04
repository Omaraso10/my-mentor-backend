package com.omar.backend.mymentor.services;

import java.util.List;
import java.util.Optional;

import com.omar.backend.mymentor.models.dto.AreaDto;
import com.omar.backend.mymentor.models.dto.MentorDto;
import com.omar.backend.mymentor.models.dto.ProfessionalDto;

public interface ProfessionalService {

    List<AreaDto> findAreaAll();

    Optional<AreaDto> findAreaById(Long id);

    List<ProfessionalDto> findProfessionalAll();

    Optional<ProfessionalDto> findProfessionalById(Long id);

    List<ProfessionalDto> findProfessionalByAreaId(Long areaId);

    public Optional<MentorDto> addProfessionalToUser(String uuid, Long professionalId);

    public void removeProfessionalFromUser(Long userProfessionalId);

    Optional<MentorDto> findMentorById(Long userProfessionalId);

}
