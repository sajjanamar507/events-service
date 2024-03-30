package com.ticketmaster.event.service.util;

import com.ticketmaster.event.service.model.EventInfo;
import com.ticketmaster.event.service.model.Venue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidationUtilTest {


    private final ValidationUtil validationUtil = new ValidationUtil();

    @Test
    public void testIsIdValid_withValidId_shouldReturnTrue() {
        String validId = "363";
        boolean result = validationUtil.isValidId(validId);
        Assertions.assertTrue(result);
    }

    @Test
    public void testIsIdValid_withInvalidId_shouldReturnFalse() {
        String invalidId = "abc";
        boolean result = validationUtil.isValidId(invalidId);
        Assertions.assertFalse(result);
    }

    @Test
    void testConvertEventToEventInfo() {
        Venue venue = TestUtils.buildVenue();
        EventInfo eventInfo = validationUtil.convertEventToEventInfo(TestUtils.buildEvent());
        assertEquals("1", eventInfo.getId());
        assertEquals("Europe/London", eventInfo.getTimeZone());
        assertEquals("O2 Academy Sheffield", eventInfo.getVenue().getName());
        assertEquals("Fusion Prog", eventInfo.getTitle());
        assertEquals(LocalDateTime.of(2024, 3, 29, 19, 55, 14, 915954), eventInfo.getStartDate());
    }
}
