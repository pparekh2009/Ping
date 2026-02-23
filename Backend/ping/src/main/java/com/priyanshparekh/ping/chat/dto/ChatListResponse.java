package com.priyanshparekh.ping.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatListResponse {

    private List<ChatResponse> chatList;

}
