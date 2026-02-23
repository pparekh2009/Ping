package com.priyanshparekh.ping.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatResponse {

    private String name;
    private Long chatId;

}
