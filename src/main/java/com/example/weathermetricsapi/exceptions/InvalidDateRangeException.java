package com.example.weathermetricsapi.exceptions;

public class InvalidDateRangeException extends RuntimeException {
    public InvalidDateRangeException() {
        super("The start date should be before the end date.");
    }
}
