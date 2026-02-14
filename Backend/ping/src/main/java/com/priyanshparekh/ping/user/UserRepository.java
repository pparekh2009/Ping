package com.priyanshparekh.ping.user;

import com.priyanshparekh.ping.search.SearchDto;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Long> {

    Optional<User> findByEmail(String email);

    List<User> findAllByEmailOrNameContainingIgnoreCase(String email, String name);
}
