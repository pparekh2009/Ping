package com.priyanshparekh.ping.chatparticipant;

import com.priyanshparekh.ping.chat.Chat;
import com.priyanshparekh.ping.chat.projection.ChatListProjection;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<@NonNull ChatParticipant, @NonNull Long> {

    @Query("""
            SELECT c FROM Chat c
             JOIN ChatParticipant cp1 ON cp1.chat = c
             JOIN ChatParticipant cp2 ON cp2.chat = c
             WHERE c.isGroup = false
             AND cp1.user.id = :user1
             AND cp2.user.id = :user2
            """)
    Optional<Chat> findOneToOneChat(@Param("user1") Long userId1, @Param("user2") Long userId2);

    List<ChatListProjection> findAllByUserId(Long userId);

    List<ChatParticipant> findAllByChatId(Long chatId);
}
