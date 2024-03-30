package com.ticketmaster.event.service.exception;

public class WebClientCustomException extends RuntimeException {
    public WebClientCustomException(String message) {
        super(message);
    }
}
