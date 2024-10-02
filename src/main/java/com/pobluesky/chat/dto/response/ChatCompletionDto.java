package com.pobluesky.chat.dto.response;

import com.pobluesky.chat.dto.request.ChatRequestMsgDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatCompletionDto {
    private String model;
    private List<ChatRequestMsgDto> messages;
}
