package com.priyanshparekh.ping.message.dto;

import com.priyanshparekh.ping.message.MessageType;
import lombok.Data;

@Data
public class MessagePayload {

    private MessageType messageType;
    private Long senderId;
    private Long chatId;
    private String payload;

}
