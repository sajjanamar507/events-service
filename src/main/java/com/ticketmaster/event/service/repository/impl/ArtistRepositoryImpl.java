package com.ticketmaster.event.service.repository.impl;

import com.ticketmaster.event.service.exception.ArtistNotFoundException;
import com.ticketmaster.event.service.model.Artist;
import com.ticketmaster.event.service.repository.ArtistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class ArtistRepositoryImpl implements ArtistRepository {
    private static final Logger logger = LoggerFactory.getLogger(ArtistRepositoryImpl.class);
    private final WebClient webClient;
    private final String artistUri;

    @Autowired
    public ArtistRepositoryImpl(WebClient webClient, @Value("${artist.uri}") String artistUri) {
        this.webClient = webClient;
        this.artistUri = artistUri;
    }

    @Override
    public Mono<List<Artist>> fetchArtists() {
        return webClient.get().uri(artistUri)
                .retrieve().bodyToFlux(Artist.class).collectList()
                .doOnSuccess(artists -> logger.info("Fetched {} artists from {}", artists.size(), artistUri))
                .onErrorResume(WebClientException.class,
                        ex -> {
                            logger.error("Error occurred while fetching artists from {}: {}", artistUri, ex.getMessage(), ex);
                            return Mono.error(new ArtistNotFoundException(ex.getMessage()));
                        });
    }

    @Override
    public Optional<Artist> findArtistById(List<Artist> artists, String artistId) {
        return artists.stream()
                .filter(artist -> artist.getId().equals(artistId))
                .findFirst();
    }
}
