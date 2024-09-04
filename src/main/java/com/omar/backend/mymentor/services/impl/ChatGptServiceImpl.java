package com.omar.backend.mymentor.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.omar.backend.mymentor.common.constant.GlobalConst;
import com.omar.backend.mymentor.models.dto.AdvisoryDetailDto;
import com.omar.backend.mymentor.models.dto.AdvisoryDto;
import com.omar.backend.mymentor.models.dto.AskProfessionalDto;
import com.omar.backend.mymentor.models.dto.ProfessionalDto;
import com.omar.backend.mymentor.models.entities.Advisory;
import com.omar.backend.mymentor.models.entities.AdvisoryDetail;
import com.omar.backend.mymentor.models.entities.UserProfessional;
import com.omar.backend.mymentor.repositories.AdvisoryRepository;
import com.omar.backend.mymentor.repositories.UserProfessionalRepository;
import com.omar.backend.mymentor.services.AdvisoryService;
import com.omar.backend.mymentor.services.ChatGptService;
import com.omar.backend.mymentor.services.ProfessionalService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Service
public class ChatGptServiceImpl implements ChatGptService {

    private static final Logger logger = LoggerFactory.getLogger(ChatGptServiceImpl.class);

    private final RestTemplate restTemplate;
    private final String apiToken;
    private final String apiUrl;
    private final String apiModel;
    private final AdvisoryService advisoryService;
    private final ProfessionalService professionalService;
    private final UserProfessionalRepository userProfessionalRepository;
    private final AdvisoryRepository advisoryRepository;

    public ChatGptServiceImpl(RestTemplate restTemplate, 
                            @Value("${chatgpt.api.token}") String apiToken, 
                            @Value("${chatgpt.api.url}") String apiUrl,
                            @Value("${chatgpt.api.model}") String apiModel, 
                            AdvisoryService advisoryService, 
                            ProfessionalService professionalService, 
                            UserProfessionalRepository userProfessionalRepository, 
                            AdvisoryRepository advisoryRepository) {
        this.restTemplate = restTemplate;
        this.apiToken = apiToken;
        this.apiUrl = apiUrl;
        this.apiModel = apiModel;
        this.advisoryService = advisoryService;
        this.professionalService = professionalService;
        this.userProfessionalRepository = userProfessionalRepository;
        this.advisoryRepository = advisoryRepository;
    }

    public Optional<AdvisoryDto> addAdviceIA(AskProfessionalDto ask) {
        Optional<AdvisoryDto> advisoryOptional;
        String response = "";
        String answer = "";
        Long professionalId = 0L;
        Optional<UserProfessional> userProfessional = userProfessionalRepository.findById(ask.getUserProfessionalId());
        if(userProfessional.isPresent()){
            professionalId = userProfessional.orElseThrow().getProfessional().getId();
        }

        response = callAPI(ask.getAsk(), professionalId, null);
        answer = extractContentFromResponse(response);

        AdvisoryDto advisoryDto = new AdvisoryDto();
        advisoryDto.setDescription(ask.getAsk().length() > 20 ? ask.getAsk().substring(0, 25) : ask.getAsk());
        advisoryDto.setModel(apiModel);
        List<AdvisoryDetailDto> advisorysDetails = new ArrayList<>();
        AdvisoryDetailDto advisoryDetailDto = new AdvisoryDetailDto();
        advisoryDetailDto.setLineNumber(1L);
        advisoryDetailDto.setQuestion(ask.getAsk());
        advisoryDetailDto.setAnswer(answer);
        advisorysDetails.add(advisoryDetailDto);
        advisoryDto.setAdvisorysDetails(advisorysDetails);
        advisoryOptional = advisoryService.addAdvisoryToUserProfessional(advisoryDto, ask.getUserProfessionalId());

        return advisoryOptional;
    }

    public Optional<AdvisoryDto> updateAdviceIA(AskProfessionalDto ask, Long id) {
        Optional<AdvisoryDto> advisoryOptionalDto;
        String response = "";
        String answer = "";
        Long professionalId = 0L;
        Optional<UserProfessional> userProfessional = userProfessionalRepository.findById(ask.getUserProfessionalId());
        if(userProfessional.isPresent()){
            professionalId = userProfessional.orElseThrow().getProfessional().getId();
        }
        
        Optional<Advisory> advisoryOptional = advisoryRepository.findById(id);
        
        Advisory advisory = new Advisory();
        if(advisoryOptional.isPresent()){
            advisory = advisoryOptional.orElseThrow();
        }
        response = callAPI(ask.getAsk(), professionalId, advisory.getAdvisorysDetails());
        answer = extractContentFromResponse(response);

        AdvisoryDetailDto detailDto = new AdvisoryDetailDto();
        Long lineNumber = (long) (advisory.getAdvisorysDetails().size() + 1);
        detailDto.setLineNumber(lineNumber);
        detailDto.setQuestion(ask.getAsk());
        detailDto.setAnswer(answer);
        advisoryOptionalDto = advisoryService.addAdvisoryDetail(detailDto, id);

        return advisoryOptionalDto;
    }

    private String callAPI(String ask, Long professional_id, List<AdvisoryDetail> advisoryDetails) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken); 

        Optional<ProfessionalDto> professionalDto = professionalService.findProfessionalById(professional_id);
        String promptSystem = professionalDto.map(ProfessionalDto::getDescription).orElse("");

        String requestBody = createRequestBody(promptSystem, ask, advisoryDetails);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    private String createRequestBody(String promptSystem, String ask, List<AdvisoryDetail> advisoryDetails) {
        String dynamicMessages = Optional.ofNullable(advisoryDetails)
                                     .orElseGet(Collections::emptyList)
                                     .stream()
                                     .sorted(Comparator.comparingLong(AdvisoryDetail::getLineNumber)) 
                                     .flatMap(detail -> Stream.of(
                                         String.format("{\"role\": \"user\", \"content\": \"%s\"}", detail.getQuestion()),
                                         String.format("%s", detail.getAnswer())
                                     ))
                                     .collect(Collectors.joining(","));

        String contextAdvice = dynamicMessages.isEmpty() ? "" : "," + dynamicMessages;

        String body = String.format("{\"model\": \"%s\", \"messages\": [{\"role\": \"system\", \"content\": \"%s\"}%s,{\"role\": \"user\", \"content\": \"%s\"}]}",
                                apiModel, promptSystem.concat(GlobalConst.INSTRUCTIONS), contextAdvice, ask);
        
        logger.debug(body);
        return body;
    }

    private String extractContentFromResponse(String responseBody) {
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            JSONArray choices = jsonResponse.getJSONArray("choices");
            if (choices.length() > 0) {
                JSONObject firstChoice = choices.getJSONObject(0); 
                JSONObject message = firstChoice.getJSONObject("message"); 
                return message.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ""; 
    }

}