package com.ticketmaster.event.service.controller;

import com.ticketmaster.event.service.model.ArtistInfo;
import com.ticketmaster.event.service.service.ArtistService;
import com.ticketmaster.event.service.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


public class ArtistControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @Mock
    private ArtistService artistService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(new ArtistController(artistService)).build();
    }

    @Test
    public void testGetArtistInfo() {
        ArtistInfo artistInfo = TestUtils.buildArtistInfo();
        when(artistService.getArtistInfo(anyString())).thenReturn(Mono.just(artistInfo));
        webTestClient.get().uri("/api/v1/artists/123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtistInfo.class)
                .isEqualTo(artistInfo);
    }

    @Test
    public void testGetArtistInfo_NotFound() {
        when(artistService.getArtistInfo(anyString())).thenReturn(Mono.empty());
        webTestClient.get().uri("/api/v1/artists/123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testGetArtistInfo_ServiceError() {
        when(artistService.getArtistInfo(anyString())).thenReturn(Mono.error(new RuntimeException()));
        webTestClient.get().uri("/api/v1/artists/123")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
