package com.example.weathermetricsapi.exceptions;

public class InvalidMetricException extends RuntimeException {
    public InvalidMetricException() {
        super("Invalid metric type. Available metrics: 'temperature', 'windspeed', 'humidity'");
    }
}
