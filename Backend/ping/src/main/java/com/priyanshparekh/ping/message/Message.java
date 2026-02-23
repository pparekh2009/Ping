package com.priyanshparekh.ping.message;

import com.priyanshparekh.ping.chat.Chat;
import com.priyanshparekh.ping.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "messages")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User user;

    @Column(name = "created_at")
    private Long createdAt;

    private String text;

    @Column(name = "message_status")
    @Enumerated(value = EnumType.STRING)
    private ChatMessageStatus chatMessageStatus;
}
