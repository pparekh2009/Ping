package com.priyanshparekh.ping.websocketconfig.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagePayload {

    private Long messageId;
    private Long chatId;
    private Long senderId;
    private String message;

}
