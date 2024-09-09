package com.omar.backend.mymentor.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
import com.omar.backend.mymentor.services.AIService;
import com.omar.backend.mymentor.services.AdvisoryService;
import com.omar.backend.mymentor.services.ProfessionalService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Service
public class AIServiceImpl implements AIService {

    private static final Logger logger = LoggerFactory.getLogger(AIServiceImpl.class);

    private final RestTemplate restTemplate;
    private final String openaiApiToken;
    private final String openaiApiUrl;
    private final String openaiApiModel;
    private final String anthropicApiToken;
    private final String anthropicApiUrl;
    private final String anthropicApiModel;
    private final String anthropicApiVersion;
    private final int maxTokens;
    private final AdvisoryService advisoryService;
    private final ProfessionalService professionalService;
    private final UserProfessionalRepository userProfessionalRepository;
    private final AdvisoryRepository advisoryRepository;

    public AIServiceImpl(RestTemplate restTemplate, 
                         @Value("${openai.api.token}") String openaiApiToken, 
                         @Value("${openai.api.url}") String openaiApiUrl,
                         @Value("${openai.api.model}") String openaiApiModel,
                         @Value("${anthropic.api.token}") String anthropicApiToken, 
                         @Value("${anthropic.api.url}") String anthropicApiUrl,
                         @Value("${anthropic.api.model}") String anthropicApiModel,
                         @Value("${anthropic.api.version}") String anthropicApiVersion,
                         @Value("${max.tokens}") int maxTokens,
                         AdvisoryService advisoryService, 
                         ProfessionalService professionalService, 
                         UserProfessionalRepository userProfessionalRepository, 
                         AdvisoryRepository advisoryRepository) {
        this.restTemplate = restTemplate;
        this.openaiApiToken = openaiApiToken;
        this.openaiApiUrl = openaiApiUrl;
        this.openaiApiModel = openaiApiModel;
        this.anthropicApiToken = anthropicApiToken;
        this.anthropicApiUrl = anthropicApiUrl;
        this.anthropicApiModel = anthropicApiModel;
        this.anthropicApiVersion = anthropicApiVersion;
        this.maxTokens = maxTokens;
        this.advisoryService = advisoryService;
        this.professionalService = professionalService;
        this.userProfessionalRepository = userProfessionalRepository;
        this.advisoryRepository = advisoryRepository;
    }

    @Override
    public Optional<AdvisoryDto> addAdviceIA(AskProfessionalDto ask) {
        Optional<AdvisoryDto> advisoryOptional;
        String response = "";
        String answer = "";
        Long professionalId = 0L;
        Optional<UserProfessional> userProfessional = userProfessionalRepository.findById(ask.getUserProfessionalId());
        if(userProfessional.isPresent()){
            professionalId = userProfessional.orElseThrow().getProfessional().getId();
        }

        response = callAPI(ask.getAsk(), professionalId, null, ask.getApiType());
        answer = extractContentFromResponse(response, ask.getApiType());

        AdvisoryDto advisoryDto = new AdvisoryDto();
        advisoryDto.setDescription(ask.getAsk().length() > 35 ? ask.getAsk().substring(0, 35).concat("...") : ask.getAsk());
        advisoryDto.setModel(getModelForApiType(ask.getApiType()));
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

    @Override
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
        response = callAPI(ask.getAsk(), professionalId, advisory.getAdvisorysDetails(), ask.getApiType());
        answer = extractContentFromResponse(response, ask.getApiType());

        AdvisoryDetailDto detailDto = new AdvisoryDetailDto();
        Long lineNumber = (long) (advisory.getAdvisorysDetails().size() + 1);
        detailDto.setLineNumber(lineNumber);
        detailDto.setQuestion(ask.getAsk());
        detailDto.setAnswer(answer);
        advisoryOptionalDto = advisoryService.addAdvisoryDetail(detailDto, id);

        return advisoryOptionalDto;
    }

    private String callAPI(String ask, Long professional_id, List<AdvisoryDetail> advisoryDetails, String apiType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        if ("openai".equals(apiType)) {
            headers.setBearerAuth(openaiApiToken);
        } else if ("anthropic".equals(apiType)) {
            headers.set("x-api-key", anthropicApiToken);
            headers.set("anthropic-version", anthropicApiVersion);
        }

        Optional<ProfessionalDto> professionalDto = professionalService.findProfessionalById(professional_id);
        String promptSystem = professionalDto.map(ProfessionalDto::getDescription).orElse("");

        String requestBody = createRequestBody(promptSystem, ask, advisoryDetails, apiType);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        
        logger.debug("Calling API: {}", getApiUrl(apiType));
        logger.debug("Request headers: {}", headers);
        logger.debug("Request body: {}", requestBody);

        try {
            ResponseEntity<String> response = restTemplate.exchange(getApiUrl(apiType), HttpMethod.POST, request, String.class);
            logger.debug("API Response status: {}", response.getStatusCode());
            logger.debug("API Response body: {}", response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("Error calling API: {}", e.getMessage());
            logger.error("Response status: {}", e.getStatusCode());
            logger.error("Response headers: {}", e.getResponseHeaders());
            logger.error("Response body: {}", e.getResponseBodyAsString());
            throw e;
        }
    }

    private String createRequestBody(String promptSystem, String ask, List<AdvisoryDetail> advisoryDetails, String apiType) {
        JSONObject requestBody = new JSONObject();
        
        if ("openai".equals(apiType)) {
            List<JSONObject> messages = new ArrayList<>();
            messages.add(new JSONObject().put("role", "system").put("content", promptSystem.concat(GlobalConst.INSTRUCTIONS)));
            
            if (advisoryDetails != null) {
                for (AdvisoryDetail detail : advisoryDetails) {
                    messages.add(new JSONObject().put("role", "user").put("content", detail.getQuestion()));
                    messages.add(new JSONObject().put("role", "assistant").put("content", detail.getAnswer()));
                }
            }
            
            messages.add(new JSONObject().put("role", "user").put("content", ask));
            
            requestBody.put("model", openaiApiModel)
                       .put("max_tokens", maxTokens)
                       .put("messages", messages);
        } else if ("anthropic".equals(apiType)) {
            JSONArray messages = new JSONArray();
            
            if (advisoryDetails != null) {
                for (AdvisoryDetail detail : advisoryDetails) {
                    messages.put(new JSONObject().put("role", "user").put("content", detail.getQuestion()));
                    messages.put(new JSONObject().put("role", "assistant").put("content", detail.getAnswer()));
                }
            }
            
            messages.put(new JSONObject().put("role", "user").put("content", ask));
            
            requestBody.put("model", anthropicApiModel)
                       .put("max_tokens", maxTokens)
                       .put("messages", messages)
                       .put("system", promptSystem.concat(GlobalConst.INSTRUCTIONS));
        }
    
        String body = requestBody.toString();
        logger.debug("Request body: {}", body);
        return body;
    }
    
    private String extractContentFromResponse(String responseBody, String apiType) {
        logger.debug("Respuesta recibida de la API {}: {}", apiType, responseBody);
        
        if (responseBody == null || responseBody.trim().isEmpty()) {
            logger.error("La respuesta de la API está vacía o es nula");
            return "";
        }
        try {
            JSONObject jsonResponse = new JSONObject(responseBody);
            if ("openai".equals(apiType)) {
                JSONArray choices = jsonResponse.getJSONArray("choices");
                if (choices.length() > 0) {
                    JSONObject choice = choices.getJSONObject(0);
                    if (choice.has("message")) {
                        JSONObject message = choice.getJSONObject("message");
                        return message.getString("content");
                    }
                }
                logger.warn("No se encontró el contenido del mensaje en la respuesta de OpenAI");
            } else if ("anthropic".equals(apiType)) {
                JSONArray content = jsonResponse.getJSONArray("content");
                if (content.length() > 0) {
                    JSONObject contentItem = content.getJSONObject(0);
                    if (contentItem.has("text")) {
                        return contentItem.getString("text");
                    }
                }
                logger.warn("No se encontró el contenido del mensaje en la respuesta de Anthropic");
            }
        } catch (JSONException e) {
            logger.error("Error al parsear la respuesta de la API: {}", e.getMessage());
            logger.error("Respuesta que causó el error: {}", responseBody);
        }
        return "";
    }

    private String getApiUrl(String apiType) {
        return "openai".equals(apiType) ? openaiApiUrl : anthropicApiUrl;
    }

    private String getModelForApiType(String apiType) {
        return "openai".equals(apiType) ? openaiApiModel : anthropicApiModel;
    }
}