package com.pobluesky.chat.service;

import com.pobluesky.chat.dto.response.ChatCompletionDto;
import com.pobluesky.global.config.RestTemplateConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GptPromptService {
    private final RestTemplateConfig restTemplateConfig;

    @Value("${openai.url.prompt}")
    private String promptUrl;

    public String prompt(ChatCompletionDto chatCompletionDto) {

        HttpHeaders headers = restTemplateConfig.httpHeaders();

        HttpEntity<ChatCompletionDto> requestEntity = new HttpEntity<>(chatCompletionDto, headers);
        ResponseEntity<String> response = restTemplateConfig
            .restTemplate()
            .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);

        return response.getBody();
    }
}
