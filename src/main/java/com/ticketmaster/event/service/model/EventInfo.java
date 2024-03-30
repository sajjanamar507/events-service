package com.ticketmaster.event.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EventInfo {
    private String id;
    private String timeZone;
    private String title;
    private LocalDateTime startDate;
    private Venue venue;
}
