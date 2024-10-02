package com.pobluesky.chat.controller;

import com.pobluesky.chat.service.ChatbotService;
import com.pobluesky.global.util.model.JsonResult;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatbotService chatbotService;

    @PostMapping("/send")
    @Operation(summary = "유저 메시지에 대한 Chatbot 응답 제공")
    public ResponseEntity<JsonResult<?>> chatWithGpt(
        @RequestHeader("Authorization") String token,
        @RequestBody String userMessage
    ) {
        JsonResult<?> response = chatbotService.processChatMessage(token, userMessage);

        return ResponseEntity.ok(response);
    }
}
