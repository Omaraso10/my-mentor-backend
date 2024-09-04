package com.omar.backend.mymentor.models.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.omar.backend.mymentor.models.dto.AdvisoryDetailDto;
import com.omar.backend.mymentor.models.dto.AdvisoryDto;
import com.omar.backend.mymentor.models.entities.Advisory;
import com.omar.backend.mymentor.models.entities.AdvisoryDetail;

@Component
public class DtoMapperAdvisoryRequest {

    private AdvisoryDto advisoryDto;
    
    private DtoMapperAdvisoryRequest() {
    }

    public static DtoMapperAdvisoryRequest builder() {
        return new DtoMapperAdvisoryRequest();
    }

    public DtoMapperAdvisoryRequest setAdvisory(AdvisoryDto advisoryDto) {
        this.advisoryDto = advisoryDto;
        return this;
    }

    public Advisory build() {
        if (advisoryDto == null) {
            throw new RuntimeException("Debe pasar el entity advisory!");
        }
        Advisory advisory = new Advisory();
        advisory.setModel(advisoryDto.getModel());
        advisory.setDescription(advisoryDto.getDescription());
        advisory.setAdvisorysDetails(getAdvisorysDetails(advisoryDto.getAdvisorysDetails()));
        return advisory;
    }

    private List<AdvisoryDetail> getAdvisorysDetails(List<AdvisoryDetailDto> advisorysDetailsDto){
        return advisorysDetailsDto.stream()
                .map(this::getAdvisoryDetail)
                .collect(Collectors.toList());
    }

    private AdvisoryDetail getAdvisoryDetail(AdvisoryDetailDto advisoryDetailDto){
        AdvisoryDetail advisoryDetail = new AdvisoryDetail();
        advisoryDetail.setId(advisoryDetailDto.getId());
        advisoryDetail.setLineNumber(advisoryDetailDto.getLineNumber());
        advisoryDetail.setQuestion(advisoryDetailDto.getQuestion());
        advisoryDetail.setAnswer(advisoryDetailDto.getAnswer());
        return advisoryDetail;
    }

}
