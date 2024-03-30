package com.ticketmaster.event.service.repository;

import com.ticketmaster.event.service.model.Venue;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface VenueRepository {
    Mono<List<Venue>> fetchVenues();

    Optional<Venue> findVenueById(List<Venue> venues, String venueId);
}
