package com.ticketmaster.event.service.controller;


import com.ticketmaster.event.service.model.ArtistInfo;
import com.ticketmaster.event.service.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/v1/artists")
public class ArtistController {

    private final ArtistService artistService;

    private static final Logger logger = LoggerFactory.getLogger(ArtistController.class);

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    /**
     * Retrieves Events of an artist based on the provided artist ID.
     * Returns a Mono containing a ResponseEntity with the artist information.
     *
     * @param artistId The unique identifier of the artist.
     * @return A Mono containing a ResponseEntity with the artist information.
     * If the artist information is found, it returns a ResponseEntity with HTTP status OK (200).
     * If no artist information is found, it returns a ResponseEntity with HTTP status NOT_FOUND (404).
     */

    @GetMapping(path = "/{artistId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<ArtistInfo>> getArtistInfo(@PathVariable String artistId) {
        return artistService.getArtistInfo(artistId)
                .doOnSuccess(info -> logger.info("Successfully retrieved events of artist for ID: {}", artistId))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .doOnError(ex -> logger.error("Error occurred while retrieving events of artist : {}", ex.getMessage(), ex));
    }

}