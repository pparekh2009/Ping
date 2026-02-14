package com.priyanshparekh.ping.message;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<@NonNull Message, @NonNull Long> {

    List<Message> findAllByChatId(Long chatId);

}
