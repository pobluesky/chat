package com.pobluesky.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.pobluesky.chat.dto.request.ChatRequestMsgDto;
import com.pobluesky.chat.dto.response.ChatCompletionDto;
import com.pobluesky.chat.entity.ProductType;
import com.pobluesky.chat.feign.Customer;
import com.pobluesky.chat.feign.UserClient;
import com.pobluesky.global.error.CommonException;
import com.pobluesky.global.error.ErrorCode;
import com.pobluesky.global.util.ResponseFactory;
import com.pobluesky.global.util.model.JsonResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class IntegratedOcrGptService {

    private final OcrService ocrService;
    private final GptPromptService gptPromptService;
    private final UserClient userClient;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    @Value("${openai.model}")
    private String openAiModel;

    @Value("${system.prompt.file.path.prefix}")
    private String promptFilePathPrefix;

    @Value("${system.prompt.file.path.suffix}")
    private String promptFilePathSuffix;

    public JsonResult<?> processFileAndStructureData(
        String token,
        Long userId,
        MultipartFile file,
        ProductType productType
    ) {
//        Customer customer = validateCustomer(token);
//
//        if (!Objects.equals(customer.getUserId(), userId)) {
//            throw new CommonException(ErrorCode.USER_NOT_MATCHED);
//        }

        List<String> textResults = ocrService.processFileAndDetectText(file);

        Map<String, Object> structuredData = getStructuredDataFromGPT(textResults, productType);

        return ResponseFactory.getSuccessJsonResult(structuredData);
    }

    private Map<String, Object> getStructuredDataFromGPT(List<String> textResults, ProductType productType) {
        String prompt = preparePromptByProductType(textResults, productType);

        List<ChatRequestMsgDto> messages = new ArrayList<>();
        messages.add(new ChatRequestMsgDto("system", "You are a helpful assistant that converts unstructured text to structured JSON."));
        messages.add(new ChatRequestMsgDto("user", prompt));

        ChatCompletionDto chatCompletionDto = new ChatCompletionDto(openAiModel, messages);
        String gptResponse = gptPromptService.prompt(chatCompletionDto);

        return extractJsonFromChatGptResponse(gptResponse);
    }

    private String preparePromptByProductType(List<String> textResults, ProductType productType) {
        String promptTemplate = loadPromptFromFile(productType.name().toLowerCase());

        StringBuilder promptBuilder = new StringBuilder(promptTemplate);
        for (String text : textResults) {
            promptBuilder.append(text).append("\n");
        }

        return promptBuilder.toString();
    }

    private String loadPromptFromFile(String productType) {
        try {
            String fileName = promptFilePathPrefix + productType + promptFilePathSuffix;
            Resource resource = resourceLoader.getResource(fileName);
            return Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CommonException(ErrorCode.SYSTEM_PROMPT_FILE_READ_ERROR);
        }
    }

    public Map<String, Object> extractJsonFromChatGptResponse(String chatGptResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(chatGptResponse);
            JsonNode contentNode = rootNode.path("choices")
                .path(0)
                .path("message")
                .path("content");

            if (contentNode.isMissingNode()) {
                return Collections.emptyMap();
            }
            return objectMapper.readValue(contentNode.asText(), new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new CommonException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    private Customer validateCustomer(String token) {
        Long userId = userClient.parseToken(token);

        Customer customer = userClient.getCustomerByIdWithoutToken(userId).getData();
        if(customer == null){
            throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }

        return customer;
    }
}
