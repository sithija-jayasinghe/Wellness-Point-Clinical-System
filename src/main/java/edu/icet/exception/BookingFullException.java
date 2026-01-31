package edu.icet.exception;

public class BookingFullException extends RuntimeException {
    public BookingFullException(String message) {
        super(message);
    }
}
