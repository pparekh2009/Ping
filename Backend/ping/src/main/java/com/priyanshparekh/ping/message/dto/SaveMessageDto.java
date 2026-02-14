package com.priyanshparekh.ping.message.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveMessageDto {

    private String text;
    private Long chatId;
    private Long senderId;
    private Long createdAt;

}
