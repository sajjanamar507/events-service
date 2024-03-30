package com.ticketmaster.event.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Artist {
    private String id;
    private String name;
    private String rank;
    private String imgSrc;
    private String url;
}