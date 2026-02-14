package com.priyanshparekh.ping.chat;

import com.priyanshparekh.ping.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "chats")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "is_group")
    @ColumnDefault(value = "false")
    private boolean isGroup;

    private String name;

}
