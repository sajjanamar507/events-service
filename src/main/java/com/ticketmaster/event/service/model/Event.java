package com.ticketmaster.event.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Event {
    private String id;
    private String timeZone;
    private String title;
    private LocalDateTime startDate;
    private List<Artist> artists;
    private Venue venue;
    private boolean hiddenFromSearch;

}