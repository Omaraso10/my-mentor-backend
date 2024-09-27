package com.omar.backend.mymentor.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omar.backend.mymentor.models.dto.AdvisoryDetailDto;
import com.omar.backend.mymentor.models.dto.AdvisoryDto;
import com.omar.backend.mymentor.models.dto.mapper.DtoMapperAdvisoryRequest;
import com.omar.backend.mymentor.models.dto.mapper.DtoMapperAdvisoryResponse;
import com.omar.backend.mymentor.models.entities.Advisory;
import com.omar.backend.mymentor.models.entities.AdvisoryDetail;
import com.omar.backend.mymentor.models.entities.UserProfessional;
import com.omar.backend.mymentor.repositories.AdvisoryRepository;
import com.omar.backend.mymentor.repositories.UserProfessionalRepository;
import com.omar.backend.mymentor.services.AdvisoryService;

@Service
public class AdvisoryServiceImpl implements AdvisoryService {

    @Autowired
    private AdvisoryRepository advisoryRepository;

    @Autowired
    private UserProfessionalRepository userProfessionalRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdvisoryDto> findAdvisoryAll() {
        List<Advisory> advisorys = (List<Advisory>) advisoryRepository.findAll();
        return advisorys
                .stream()
                .map(a -> DtoMapperAdvisoryResponse.builder().setAdvisory(a).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AdvisoryDto> findAdvisoryById(Long id) {
        return advisoryRepository.findById(id)
                .map(a -> DtoMapperAdvisoryResponse
                .builder()
                .setAdvisory(a)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdvisoryDto> findByUserProfessionalId(Long userProfessionalId) {
        List<Advisory> advisorys = (List<Advisory>) advisoryRepository.findByUserProfessionalId(userProfessionalId);
        return advisorys
                .stream()
                .map(a -> DtoMapperAdvisoryResponse.builder().setAdvisory(a).build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<AdvisoryDto> addAdvisoryToUserProfessional(AdvisoryDto advisoryDto, Long userProfessionalId) {
        Advisory advisory = DtoMapperAdvisoryRequest.builder().setAdvisory(advisoryDto).build();
        UserProfessional userProfessional = userProfessionalRepository.findById(userProfessionalId)
                .orElseThrow(() -> new RuntimeException("userProfessional not found"));
        advisory.setUserProfessional(userProfessional);
        return Optional.ofNullable(DtoMapperAdvisoryResponse.builder().setAdvisory(advisoryRepository.save(advisory)).build());
    }

    @Override
    @Transactional
    public Optional<AdvisoryDto> addAdvisoryDetail(AdvisoryDetailDto detailDto, Long advisoryId) {
        Optional<Advisory> advisoryOpt = advisoryRepository.findById(advisoryId);
        if (advisoryOpt.isPresent()) {
            Advisory advisory = advisoryOpt.get();
            AdvisoryDetail detail = new AdvisoryDetail();
            detail.setLineNumber(detailDto.getLineNumber());
            detail.setQuestion(detailDto.getQuestion());
            detail.setAnswer(detailDto.getAnswer());
            detail.setModel(detailDto.getModel());
            advisory.getAdvisorysDetails().add(detail);
            return Optional.ofNullable(DtoMapperAdvisoryResponse.builder().setAdvisory(advisoryRepository.save(advisory)).build());
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void removeAdvisoryById(Long id) {
        advisoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void removeAdvisoryToUserProfessional(Long userProfessionalId) {
        advisoryRepository.deleteByUserProfessionalId(userProfessionalId);
    }
}