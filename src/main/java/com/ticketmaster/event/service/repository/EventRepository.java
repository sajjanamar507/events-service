package com.ticketmaster.event.service.repository;

import com.ticketmaster.event.service.model.Event;
import reactor.core.publisher.Mono;

import java.util.List;

public interface EventRepository {
    Mono<List<Event>> fetchEvents();

    Mono<List<Event>> filterEventsByArtistId(Mono<List<Event>> eventsMono, String artistId);
}
