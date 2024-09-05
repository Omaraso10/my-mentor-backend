package com.omar.backend.mymentor.services;

import java.util.Optional;

import com.omar.backend.mymentor.models.dto.AdvisoryDto;
import com.omar.backend.mymentor.models.dto.AskProfessionalDto;

public interface AIService {

    public Optional<AdvisoryDto> addAdviceIA(AskProfessionalDto ask);

    public Optional<AdvisoryDto> updateAdviceIA(AskProfessionalDto ask, Long id);

}
