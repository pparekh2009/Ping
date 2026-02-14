package com.priyanshparekh.ping.search;

import com.priyanshparekh.ping.user.UserRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SearchService {

    private final UserRepository userRepository;

    public List<SearchDto> search(String query) {
        log.info("searchService: search: query: {}", query);
        return userRepository.findAllByEmailOrNameContainingIgnoreCase(query, query).stream()
                .map(user ->
                        {
                            log.info("searchService: search: user name: {}", user.getName());
                            return SearchDto.builder()
                                    .id(user.getId())
                                    .name(user.getName())
                                    .build();
                        }
                ).toList();
    }
}
