package com.ticketmaster.event.service.exceptionhandling;

import com.ticketmaster.event.service.exception.ArtistNotFoundException;
import com.ticketmaster.event.service.exception.WebClientCustomException;
import com.ticketmaster.event.service.model.ErrorResponse;
import com.ticketmaster.event.service.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.ticketmaster.event.service"})
public class RestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(ArtistNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleArtistNotFoundException(ArtistNotFoundException ex) {
        logger.error(Constants.ARTIST_NOT_FOUND, ex);
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }



    @ExceptionHandler(WebClientCustomException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleWebClientCustomException(WebClientCustomException ex) {
        logger.error(Constants.INVALID_URL, ex);
        ErrorResponse errorResponse = new ErrorResponse(Constants.INVALID_URL);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleThrowable(Throwable ex) {
        logger.error("Error occurred : {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(Constants.TRY_AFTER_SOME_TIME);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
