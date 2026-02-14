package com.priyanshparekh.ping.chat;

import com.priyanshparekh.ping.chat.projection.ChatProjection;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<@NonNull Chat, @NonNull Long> {


    @Query(value = """
            SELECT c.id, u.name
            FROM chats c
            JOIN chat_participants cp
            ON c.id = cp.chat_id
            JOIN users u
            ON u.id = cp.id
            WHERE u.id != :userId
            """, nativeQuery = true)
    List<ChatProjection> findAllChats(@Param("userId") Long userId);
}
