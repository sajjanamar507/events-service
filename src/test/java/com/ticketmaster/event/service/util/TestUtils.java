package com.ticketmaster.event.service.util;

import com.ticketmaster.event.service.model.*;

import java.time.LocalDateTime;
import java.util.Collections;


public class TestUtils {

    public static Artist buildArtist() {
        return Artist.builder()
                .id("21")
                .name("HRH Prog")
                .rank("1")
                .imgSrc("//some-base-url/hrh-prog.jpg")
                .url("/hrh-prog-tickets/artist/21")
                .build();

    }

    public static Venue buildVenue() {
        return Venue.builder()
                .name("O2 Academy Sheffield")
                .url("/o2-academy-sheffield-tickets-sheffield/venue/41")
                .city("Sheffield")
                .id("41")
                .build();

    }

    public static Event buildEvent() {
        return Event.builder()
                .id("1")
                .timeZone("Europe/London")
                .title("Fusion Prog")
                .startDate(LocalDateTime.of(2024, 3, 29, 19, 55, 14, 915954))
                .artists(Collections.singletonList((buildArtist())))
                .venue(buildVenue())
                .hiddenFromSearch(false)
                .build();
    }

    public static ArtistInfo buildArtistInfo() {
        return ArtistInfo.builder()
                .artist(buildArtist())
                .events(Collections.singletonList((buildEventInfo())))
                .build();
    }

    public static EventInfo buildEventInfo() {
        return EventInfo.builder().id("1").timeZone("Europe/London")
                .title("Fusion Prog")
                .startDate(LocalDateTime.of(2024, 3, 29, 19, 55, 14, 915954))
                .venue(buildVenue()).build();


    }
}
