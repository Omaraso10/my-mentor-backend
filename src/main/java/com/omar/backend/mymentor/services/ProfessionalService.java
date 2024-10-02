package com.omar.backend.mymentor.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.omar.backend.mymentor.models.dto.AreaDto;
import com.omar.backend.mymentor.models.dto.MentorDto;
import com.omar.backend.mymentor.models.dto.ProfessionalDto;

public interface ProfessionalService {

    List<AreaDto> findAreaAll();

    public List<AreaDto> findAreaGeneral();

    Optional<AreaDto> findAreaById(Long id);

    List<ProfessionalDto> findProfessionalAll();

    public Page<ProfessionalDto> findAdvisorysGeneral(Pageable pageable);

    public Page<ProfessionalDto> findAll(Pageable pageable);

    Optional<ProfessionalDto> findProfessionalById(Long id);

    public ProfessionalDto createProfessional(ProfessionalDto professionalDto);

    public Optional<ProfessionalDto> updateProfessional(Long id, ProfessionalDto professionalDto);

    public void deleteProfessional(Long id);

    List<ProfessionalDto> findProfessionalByAreaId(Long areaId);

    public Optional<MentorDto> addProfessionalToUser(String uuid, Long professionalId);

    public void removeProfessionalFromUser(Long userProfessionalId);

    Optional<MentorDto> findMentorById(Long userProfessionalId);

}
