package com.priyanshparekh.ping.search;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchDto {

    private Long id;
    private String name;

}
