package com.ticketmaster.event.service.service.impl;

import com.ticketmaster.event.service.exception.ArtistNotFoundException;
import com.ticketmaster.event.service.exception.WebClientCustomException;
import com.ticketmaster.event.service.repository.ArtistRepository;
import com.ticketmaster.event.service.repository.EventRepository;
import com.ticketmaster.event.service.repository.VenueRepository;
import com.ticketmaster.event.service.service.ArtistService;
import com.ticketmaster.event.service.util.TestUtils;
import com.ticketmaster.event.service.util.ValidationUtil;

import com.ticketmaster.event.service.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ArtistServiceTest {

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private ValidationUtil validationUtil;

    private ArtistService artistService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        artistService = new ArtistServiceImpl(artistRepository, eventRepository, venueRepository, validationUtil);
    }

    @Test
    public void testGetArtistInfo() {
        String artistId = "21";
        Artist artist = TestUtils.buildArtist();
        Venue venue = TestUtils.buildVenue();
        Event event = TestUtils.buildEvent();
        List<Event> events = Collections.singletonList(event);
        List<Venue> venues = Collections.singletonList(venue);
        ArtistInfo expectedArtistInfo = TestUtils.buildArtistInfo();
        when(validationUtil.isValidId(artistId)).thenReturn(true);
        when(artistRepository.fetchArtists()).thenReturn(Mono.just(Arrays.asList(artist)));
        when(artistRepository.findArtistById(anyList(), eq(artistId))).thenReturn(Optional.of(artist));
        when(eventRepository.fetchEvents()).thenReturn(Mono.just(events));
        when(eventRepository.filterEventsByArtistId(any(Mono.class), eq(artistId))).thenReturn(Mono.just(events));
        when(venueRepository.fetchVenues()).thenReturn(Mono.just(venues));
        when(venueRepository.findVenueById(anyList(), anyString())).thenAnswer(invocation -> {
            String venueId = invocation.getArgument(1);
            return venues.stream().filter(v -> v.getId().equals(venueId)).findFirst();
        });

        when(validationUtil.convertEventToEventInfo(event)).thenReturn(EventInfo.builder()
                .id(event.getId())
                .title(event.getTitle())
                .venue(venue)
                .startDate(event.getStartDate())
                .timeZone(event.getTimeZone())
                .build());

        StepVerifier.create(artistService.getArtistInfo(artistId))
                .expectNext(expectedArtistInfo)
                .verifyComplete();

    }

    @Test
    void testGiven_InvalidArtistId_ThrowsArtistNotFoundException() {
        String artistId = "1";

        when(validationUtil.isValidId(artistId)).thenReturn(true);
        List<Artist> artists = Collections.emptyList();

        when(artistRepository.fetchArtists()).thenReturn(Mono.just(artists));

        Mono<ArtistInfo> result = artistService.getArtistInfo(artistId);

        StepVerifier.create(result)
                .expectError(ArtistNotFoundException.class)
                .verify();
    }


    @Test
    void testGiven_NullArtistId_ThrowsIllegalArgumentException() {
        Mono<ArtistInfo> result = artistService.getArtistInfo(null);

        StepVerifier.create(result)
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    public void testGetArtistInfo_shouldThrow_WebClientCustomException() {
        String artistId = "480";
        when(validationUtil.isValidId(artistId)).thenReturn(true);
        when(artistRepository.fetchArtists()).thenReturn(Mono.error(new WebClientCustomException("Error occurred while fetching data")));
        when(eventRepository.fetchEvents()).thenReturn(Mono.just(new ArrayList<>()));
        when(venueRepository.fetchVenues()).thenReturn(Mono.just(new ArrayList<>()));

        Mono<ArtistInfo> artistInfoMono = artistService.getArtistInfo(artistId);

        StepVerifier.create(artistInfoMono)
                .expectError(WebClientCustomException.class)
                .verify();
    }
}
