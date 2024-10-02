package com.pobluesky.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRequestMsgDto {
    private String role;
    private String content;
}
