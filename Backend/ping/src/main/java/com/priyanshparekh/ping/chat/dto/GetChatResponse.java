package com.priyanshparekh.ping.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetChatResponse {

    private Long chatId;
    private String name;

}
