package com.priyanshparekh.ping.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatListResponse {

    Long chatId;
    String name;

}
