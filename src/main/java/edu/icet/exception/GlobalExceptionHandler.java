package edu.icet.exception;

import edu.icet.dto.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleAnyException(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        APIResponse response = new APIResponse("Something went wrong", null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //  409 - Already exists (email duplicate etc.)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<APIResponse> handleAlreadyExists(ResourceAlreadyExistsException ex) {
        APIResponse response = new APIResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    //  404 - Not found (doctor not found, appointment not found etc.)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> handleNotFound(ResourceNotFoundException ex) {
        APIResponse response = new APIResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //  400 - Illegal arguments (bad input, invalid state)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse> handleIllegalArgument(IllegalArgumentException ex) {
        APIResponse response = new APIResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //  400 - Business rule violation
    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<APIResponse> handleInvalidOperation(InvalidOperationException ex) {
        APIResponse response = new APIResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //  409 - Capacity conflict
    @ExceptionHandler(BookingFullException.class)
    public ResponseEntity<APIResponse> handleBookingFull(BookingFullException ex) {
        APIResponse response = new APIResponse(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
