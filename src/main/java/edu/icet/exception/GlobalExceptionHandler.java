package edu.icet.exception;

import edu.icet.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse> handleRuntimeException(RuntimeException ex) {

        String message = ex.getMessage();

        APIResponse response = new APIResponse(message, null);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}