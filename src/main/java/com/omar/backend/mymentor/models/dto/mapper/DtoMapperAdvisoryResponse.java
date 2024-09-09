package com.omar.backend.mymentor.models.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.omar.backend.mymentor.models.dto.AdvisoryDetailDto;
import com.omar.backend.mymentor.models.dto.AdvisoryDto;
import com.omar.backend.mymentor.models.entities.Advisory;
import com.omar.backend.mymentor.models.entities.AdvisoryDetail;

@Component
public class DtoMapperAdvisoryResponse {

    private Advisory advisory;
    
    private DtoMapperAdvisoryResponse() {
    }

    public static DtoMapperAdvisoryResponse builder() {
        return new DtoMapperAdvisoryResponse();
    }

    public DtoMapperAdvisoryResponse setAdvisory(Advisory advisory) {
        this.advisory = advisory;
        return this;
    }

    public AdvisoryDto build() {
        if (advisory == null) {
            throw new RuntimeException("Debe pasar el entity advisory!");
        }
        AdvisoryDto advisoryDto = new AdvisoryDto();
        advisoryDto.setId(advisory.getId());
        advisoryDto.setDescription(advisory.getDescription());
        advisoryDto.setModel(advisory.getModel());
        advisoryDto.setAdvisorysDetails(getAdvisorysDetails(advisory.getAdvisorysDetails()));
        return advisoryDto;
    }

    private List<AdvisoryDetailDto> getAdvisorysDetails(List<AdvisoryDetail> advisorysDetails){
        return advisorysDetails.stream()
                .map(this::getAdvisoryDetail)
                .collect(Collectors.toList());
    }

    private AdvisoryDetailDto getAdvisoryDetail(AdvisoryDetail advisoryDetail){
        AdvisoryDetailDto advisoryDetailDto = new AdvisoryDetailDto();
        advisoryDetailDto.setId(advisoryDetail.getId());
        advisoryDetailDto.setLineNumber(advisoryDetail.getLineNumber());
        advisoryDetailDto.setQuestion(advisoryDetail.getQuestion());
        advisoryDetailDto.setAnswer(advisoryDetail.getAnswer());
        return advisoryDetailDto;
    }

}
