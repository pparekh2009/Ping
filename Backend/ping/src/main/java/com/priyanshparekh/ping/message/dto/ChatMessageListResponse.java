package com.priyanshparekh.ping.message.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatMessageListResponse {

    private List<ChatMessageResponse> messageList;

}
