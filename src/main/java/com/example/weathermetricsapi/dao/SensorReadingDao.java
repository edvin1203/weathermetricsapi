package com.example.weathermetricsapi.dao;

import com.example.weathermetricsapi.exceptions.InvalidDateRangeException;
import com.example.weathermetricsapi.exceptions.InvalidMetricException;
import com.example.weathermetricsapi.model.SensorReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Repository
public class SensorReadingDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertSensorReading(SensorReading sensorReading) throws InvalidMetricException {
        if (!isValidMetric(sensorReading.getMetricType())) {
            throw new InvalidMetricException();
        } else {
            String sql = "INSERT INTO weather_sensor_readings (sensor_id, reading_date, metric_value, metric_type) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, sensorReading.getSensorId(), sensorReading.getReadingDate(),
                    sensorReading.getMetricValue(), sensorReading.getMetricType().toLowerCase());
        }

    }

    public double getAverageMetricValue(String metric, LocalDateTime startDate, LocalDateTime endDate) throws InvalidMetricException, InvalidDateRangeException {
        if (!isValidMetric(metric)) {
            throw new InvalidMetricException();
        } else if (!isValidDateRange(startDate, endDate)) {
            throw new InvalidDateRangeException();
        } else {
            String query = "SELECT AVG(metric_value) FROM weather_sensor_readings WHERE metric_type = ? AND reading_date BETWEEN ? AND ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, Double.class, metric, startDate, endDate)).orElse(0.0);
        }
    }

    public Double getAverageMetricValueBySensor(String sensorId, String metric, LocalDateTime startDate, LocalDateTime endDate) throws InvalidMetricException, InvalidDateRangeException {
        if (!isValidMetric(metric)) {
            throw new InvalidMetricException();
        } else if (!isValidDateRange(startDate, endDate)) {
            throw new InvalidDateRangeException();
        } else {
            String query = "SELECT AVG(metric_value) FROM weather_sensor_readings WHERE sensor_id = ? AND reading_date BETWEEN ? AND ? AND metric_type = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, Double.class, sensorId, startDate, endDate, metric)).orElse(0.0);
        }
    }

    void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private boolean isValidDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return endDate.isAfter(startDate);
    }

    private boolean isValidMetric(String metric) {
        String[] validMetrics = {"humidity", "windspeed", "temperature"};
        return Arrays.asList(validMetrics).contains(metric.toLowerCase());
    }

}
