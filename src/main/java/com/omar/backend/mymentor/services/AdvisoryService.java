package com.omar.backend.mymentor.services;

import java.util.List;
import java.util.Optional;

import com.omar.backend.mymentor.models.dto.AdvisoryDetailDto;
import com.omar.backend.mymentor.models.dto.AdvisoryDto;

public interface AdvisoryService {

    List<AdvisoryDto> findAdvisoryAll();

    Optional<AdvisoryDto> findAdvisoryById(Long id);

    List<AdvisoryDto> findByUserProfessionalId(Long userProfessionalId);

    Optional<AdvisoryDto> addAdvisoryToUserProfessional(AdvisoryDto advisoryDto, Long userProfessionalId);

    Optional<AdvisoryDto> addAdvisoryDetail(AdvisoryDetailDto detailDto, Long advisoryId);

    public void removeAdvisoryById(Long id);

    public void removeAdvisoryToUserProfessional(Long userProfessionalId);

}
