package com.priyanshparekh.ping.message;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/messages")
    public ResponseEntity<@NonNull List<MessageHistoryResponse>> getMessages(@RequestParam("chat_id") Long chatId) {
        return ResponseEntity.ok(messageService.getMessages(chatId));
    }

}
