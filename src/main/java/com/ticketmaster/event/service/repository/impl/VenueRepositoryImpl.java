package com.ticketmaster.event.service.repository.impl;

import com.ticketmaster.event.service.model.Venue;
import com.ticketmaster.event.service.repository.VenueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class VenueRepositoryImpl implements VenueRepository {

    private final Logger logger = LoggerFactory.getLogger(VenueRepositoryImpl.class);
    private final WebClient webClient;
    private final String venueUri;

    @Autowired
    public VenueRepositoryImpl(WebClient webClient,@Value("${venue.uri}") String venueUri) {
        this.webClient = webClient;
        this.venueUri = venueUri;
    }
    @Override
    public Mono<List<Venue>> fetchVenues() {
        logger.info("Fetching venues from URI: {}", venueUri);
        return webClient.get().uri(venueUri).retrieve().bodyToFlux(Venue.class).collectList()
                .onErrorReturn(Collections.emptyList());
    }

    @Override
    public Optional<Venue> findVenueById(List<Venue> venues, String venueId) {
        logger.info("Searching venue with ID: {}", venueId);
        return venues.stream()
                .filter(venue -> venue.getId().equals(venueId))
                .findFirst();
    }
}
