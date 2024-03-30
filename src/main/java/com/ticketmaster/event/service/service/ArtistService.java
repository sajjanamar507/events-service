package com.ticketmaster.event.service.service;

import com.ticketmaster.event.service.model.ArtistInfo;
import reactor.core.publisher.Mono;

public interface ArtistService {
    public Mono<ArtistInfo> getArtistInfo(String artistId);
}
