package com.ticketmaster.event.service.service.impl;


import com.ticketmaster.event.service.exception.WebClientCustomException;
import com.ticketmaster.event.service.repository.ArtistRepository;
import com.ticketmaster.event.service.repository.EventRepository;
import com.ticketmaster.event.service.repository.VenueRepository;
import com.ticketmaster.event.service.service.ArtistService;
import com.ticketmaster.event.service.util.Constants;
import com.ticketmaster.event.service.util.ValidationUtil;
import com.ticketmaster.event.service.exception.ArtistNotFoundException;
import com.ticketmaster.event.service.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {
    private static final Logger logger = LoggerFactory.getLogger(ArtistServiceImpl.class);
    private final ArtistRepository artistRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    private final ValidationUtil validationUtil;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository, EventRepository eventRepository,
                             VenueRepository venueRepository, ValidationUtil validationUtil) {
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.validationUtil = validationUtil;
    }

    /**
     * Retrieves information about an artist based on the provided artist ID.
     * Returns a Mono containing the artist information.
     *
     * @param artistId The unique identifier of the artist.
     * @return A Mono containing the artist information.
     * If the artist ID is valid and the artist is found, it returns a Mono with the artist information.
     * If the artist ID is not valid or the artist is not found, it returns a Mono with an error.
     * @throws IllegalArgumentException If the artistId is null or empty.
     * @throws WebClientCustomException If an error occurs while fetching the data.
     */

    @Override
    public Mono<ArtistInfo> getArtistInfo(String artistId) {

        if (validationUtil.isValidId(artistId)) {
            Mono<List<Artist>> artistsMono = artistRepository.fetchArtists();
            Mono<List<Event>> eventsMono = eventRepository.fetchEvents();
            Mono<List<Venue>> venuesMono = venueRepository.fetchVenues();
            return artistsMono.flatMap(artists -> {
                        Artist artist = artistRepository.findArtistById(artists, artistId)
                                .orElseThrow(() -> new ArtistNotFoundException("Artist not found : {}" + artistId));
                        Mono<List<Event>> filteredEventsMono = eventRepository.filterEventsByArtistId(eventsMono, artistId);
                        return Mono.zip(venuesMono, filteredEventsMono)
                                .map(tuple -> {
                                    List<Venue> venues = tuple.getT1();
                                    List<Event> events = tuple.getT2();
                                    for (Event event : events) {
                                        Optional<Venue> venueOpt = venueRepository.findVenueById(venues, event.getVenue().getId());
                                        venueOpt.ifPresent(event::setVenue);
                                    }
                                    return ArtistInfo.builder().artist(artist).events(convertEventToEventInfo(events)).build();
                                });
                    }).onErrorResume(WebClientResponseException.class, ex -> {
                        logger.error("Error occurred while fetching data : {}", ex.getMessage(), ex);
                        String errorMessage = "Error occurred while fetching data: " + ex.getMessage();
                        return Mono.error(new WebClientCustomException(errorMessage));
                    })
                    .doOnSuccess(artistInfo -> logger.info("Successfully retrieved artist information for artistId {}", artistId));

        } else {
            return Mono.error(new IllegalArgumentException(Constants.INVALID_ARGUMENTS + artistId));
        }
    }

    private List<EventInfo> convertEventToEventInfo(List<Event> events) {
        return events.stream()
                .map(validationUtil::convertEventToEventInfo) // Map Event objects to EventInfo objects
                .collect(Collectors.toList());
    }
}