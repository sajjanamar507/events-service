package com.ticketmaster.event.service.util;

import com.ticketmaster.event.service.model.Event;
import com.ticketmaster.event.service.model.EventInfo;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class ValidationUtil {
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);
    public  boolean isValidId(String artistId) {
        try {
            if(!StringUtils.isBlank(artistId))
              Integer.parseInt(artistId);
               return true;
        } catch (NumberFormatException ex) {
            logger.error("Error parsing artistId: {}", ex);
            return false;
        }
    }

    public EventInfo convertEventToEventInfo(Event event) {
        return EventInfo.builder().id(event.getId())
                .timeZone(event.getTimeZone())
                .title(event.getTitle())
                .venue(event.getVenue())
                .startDate(event.getStartDate())
                .build();
    }
}
