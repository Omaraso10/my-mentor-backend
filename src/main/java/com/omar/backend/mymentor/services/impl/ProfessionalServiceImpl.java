package com.omar.backend.mymentor.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omar.backend.mymentor.models.dto.AreaDto;
import com.omar.backend.mymentor.models.dto.MentorDto;
import com.omar.backend.mymentor.models.dto.ProfessionalDto;
import com.omar.backend.mymentor.models.dto.mapper.DtoMapperAreaResponse;
import com.omar.backend.mymentor.models.dto.mapper.DtoMapperMentorResponse;
import com.omar.backend.mymentor.models.dto.mapper.DtoMapperProfessionalResponse;
import com.omar.backend.mymentor.models.entities.Area;
import com.omar.backend.mymentor.models.entities.Professional;
import com.omar.backend.mymentor.models.entities.UserProfessional;
import com.omar.backend.mymentor.repositories.AreaRepository;
import com.omar.backend.mymentor.repositories.ProfessionalRepository;
import com.omar.backend.mymentor.repositories.UserProfessionalRepository;
import com.omar.backend.mymentor.services.AdvisoryService;
import com.omar.backend.mymentor.services.ProfessionalService;

@Service
public class ProfessionalServiceImpl implements ProfessionalService{

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private UserProfessionalRepository userProfessionalRepository;

    @Autowired
    private AdvisoryService advisoryService;

    @Override
    @Transactional(readOnly = true)
    public List<AreaDto> findAreaAll() {
        List<Area> areas = (List<Area>) areaRepository.findAll();
        return areas
                .stream()
                .filter(area -> !area.getId().equals(1L)) 
                .map(a -> DtoMapperAreaResponse.builder().setArea(a).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AreaDto> findAreaById(Long id) {
        return areaRepository.findById(id)
                .map(a -> DtoMapperAreaResponse
                .builder()
                .setArea(a)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalDto> findProfessionalAll() {
        List<Professional> professionals = (List<Professional>) professionalRepository.findAll();
        return professionals
                .stream()
                .filter(p -> !p.getId().equals(1L)) 
                .map(p -> DtoMapperProfessionalResponse.builder().setProfessional(p).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProfessionalDto> findProfessionalById(Long id) {
        return professionalRepository.findById(id)
                .map(p -> DtoMapperProfessionalResponse
                .builder()
                .setProfessional(p)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalDto> findProfessionalByAreaId(Long areaId) {
        List<Professional> professionals = (List<Professional>) professionalRepository.findByAreaId(areaId);
        return professionals
                .stream()
                .map(p -> DtoMapperProfessionalResponse.builder().setProfessional(p).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<MentorDto> addProfessionalToUser(String uuid, Long professionalId) {
        UserProfessional userProfessional = new UserProfessional();
        Optional<Professional> professionalOptional;
        MentorDto mentorDto = new MentorDto();
        Optional<Professional> professionalOp = professionalRepository.findById(professionalId);
        if(professionalOp.isPresent()){
            userProfessional.setProfessional(professionalOp.orElseThrow());
        }
        userProfessional.setUuid(uuid);
        userProfessional = userProfessionalRepository.save(userProfessional);
        mentorDto = DtoMapperMentorResponse.builder().setUserProfessional(userProfessional).build();
        professionalOptional = professionalRepository.findById(professionalId);
        if(professionalOptional.isPresent()){
            mentorDto.setProfessional(DtoMapperProfessionalResponse.builder().setProfessional(professionalOptional.orElseThrow()).build());
        }
        return Optional.ofNullable(mentorDto);
    }

    @Override
    @Transactional
    public void removeProfessionalFromUser(Long userProfessionalId) {
        //Eliminar advisorys primero
        advisoryService.removeAdvisoryToUserProfessional(userProfessionalId);
        //Eliminar UserProfessional
        userProfessionalRepository.deleteById(userProfessionalId);
        
    }

    @Override
    public Optional<MentorDto> findMentorById(Long userProfessionalId) {
        Optional<UserProfessional> userProfessionalOptional = userProfessionalRepository.findById(userProfessionalId);
        MentorDto mentorDto = null;
        if(userProfessionalOptional.isPresent()){
            mentorDto = DtoMapperMentorResponse.builder().setUserProfessional(userProfessionalOptional.orElseThrow()).build();
        }
        return Optional.ofNullable(mentorDto);
    }

}
