package com.priyanshparekh.ping.chat;

import com.priyanshparekh.ping.MessageResponse;
import com.priyanshparekh.ping.chat.dto.AddChatRequest;
import com.priyanshparekh.ping.chat.dto.GetChatResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<@NonNull MessageResponse> addChat(@RequestBody AddChatRequest addChatRequest) {
        chatService.addChat(addChatRequest);
        return ResponseEntity.ok(MessageResponse.builder().message("Chat Added").build());
    }

    @GetMapping("/chats")
    public ResponseEntity<@NonNull List<GetChatResponse>> getAllChats() {
        return ResponseEntity.ok(chatService.getAllChats());
    }
}
