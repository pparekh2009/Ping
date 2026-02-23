package com.priyanshparekh.ping.message.dto;

import com.priyanshparekh.ping.message.ChatMessageStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageResponse {

    private String message;
    private Long messageId;
    private Long chatId;
    private Long senderId;
    private ChatMessageStatus chatMessageStatus;

}
