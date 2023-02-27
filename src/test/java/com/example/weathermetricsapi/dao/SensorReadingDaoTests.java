package com.example.weathermetricsapi.dao;

import com.example.weathermetricsapi.exceptions.InvalidDateRangeException;
import com.example.weathermetricsapi.exceptions.InvalidMetricException;
import com.example.weathermetricsapi.model.SensorReading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorReadingDaoTests {

    private SensorReadingDao sensorReadingDao;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sensorReadingDao = new SensorReadingDao();
        sensorReadingDao.setJdbcTemplate(jdbcTemplate);
    }

    @Test
    void insertSensorReadingShouldBeSuccessful() throws InvalidMetricException {
        // Arrange
        SensorReading sensorReading = new SensorReading("S1", LocalDateTime.now(), 24.3, "temperature");

        // Act
        sensorReadingDao.insertSensorReading(sensorReading);

        // Assert
        verify(jdbcTemplate, times(1)).update(anyString(), eq(sensorReading.getSensorId()),
                eq(sensorReading.getReadingDate()), eq(sensorReading.getMetricValue()), eq(sensorReading.getMetricType().toLowerCase()));
    }

    @Test
    void insertSensorReadingShouldReturnInvalidMetricException() {
        // Arrange
        SensorReading sensorReading = new SensorReading("S1", LocalDateTime.now(), 24.3, "invalid-metric");

        // Act and assert
        assertThrows(InvalidMetricException.class, () -> sensorReadingDao.insertSensorReading(sensorReading));
        verify(jdbcTemplate, never()).update(anyString(), any(), any(), any(), any());
    }

    @Test
    void getAverageMetricValueShouldReturnMetricValue() throws InvalidMetricException, InvalidDateRangeException {
        // Arrange
        String metric = "temperature";
        LocalDateTime startDate = LocalDateTime.of(2022, 2, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 2, 28, 0, 0, 0);
        Double expectedAverage = 23.4;

        when(jdbcTemplate.queryForObject(anyString(), eq(Double.class), eq(metric), eq(startDate), eq(endDate)))
                .thenReturn(expectedAverage);

        // Act
        double actualAverage = sensorReadingDao.getAverageMetricValue(metric, startDate, endDate);

        // Assert
        verify(jdbcTemplate, times(1)).queryForObject(anyString(), eq(Double.class), eq(metric), eq(startDate), eq(endDate));
        assertEquals(expectedAverage, actualAverage);
    }

    @Test
    void getAverageMetricValueShouldReturnInvalidMetricException() {
        // Arrange
        String metric = "invalidmetric";
        LocalDateTime startDate = LocalDateTime.of(2022, 2, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2022, 2, 28, 0, 0, 0);

        // Act and assert
        assertThrows(InvalidMetricException.class, () -> sensorReadingDao.getAverageMetricValue(metric, startDate, endDate));
        verify(jdbcTemplate, never()).queryForObject(anyString(), eq(Double.class), any(), any(), any());
    }

}