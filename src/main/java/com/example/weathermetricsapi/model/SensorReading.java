package com.example.weathermetricsapi.model;

import java.time.LocalDateTime;
import java.util.Date;

public class SensorReading {
    private String sensorId;
    private LocalDateTime readingDate;
    private double metricValue;
    private String metricType;

    public SensorReading(String sensorId, LocalDateTime readingDate, double metricValue, String metricType) {
        this.sensorId = sensorId;
        this.readingDate = readingDate;
        this.metricValue = metricValue;
        this.metricType = metricType;

    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public LocalDateTime getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(LocalDateTime readingDate) {
        this.readingDate = readingDate;
    }

    public double getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(double metricValue) {
        this.metricValue = metricValue;
    }

    public String getMetricType() {
        return metricType;
    }

    public void setMetricType(String metricType) {
        this.metricType = metricType;
    }
}
