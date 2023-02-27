package com.example.weathermetricsapi.controller;

import com.example.weathermetricsapi.dao.SensorReadingDao;
import com.example.weathermetricsapi.exceptions.InvalidDateRangeException;
import com.example.weathermetricsapi.exceptions.InvalidMetricException;
import com.example.weathermetricsapi.model.SensorReading;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class SensorReadingControllerTest {

    @Mock
    private SensorReadingDao sensorReadingDao;

    @InjectMocks
    private SensorReadingController controller;

    private static final String INVALID_METRIC_MESSAGE = "Invalid metric type. Available metrics: 'temperature', 'windspeed', 'humidity'";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createSensorReadingShouldReturnOk() throws InvalidMetricException {
        SensorReading sensorReading = new SensorReading("sensor0", LocalDateTime.now().minusHours(5), 31.1, "temperature");
        ResponseEntity<?> response = controller.createSensorReading(sensorReading);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void createSensorReadingShouldReturnBadRequest() throws InvalidMetricException {
        SensorReading sensorReading = new SensorReading("sensor0", LocalDateTime.now().minusHours(5), 31.1, "invalidmetric");
        doThrow(new InvalidMetricException()).when(sensorReadingDao).insertSensorReading(sensorReading);
        ResponseEntity<?> response = controller.createSensorReading(sensorReading);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(INVALID_METRIC_MESSAGE, response.getBody());
    }

    @Test
    public void getAverageMetricValueForAllSensorsShouldReturnOk() throws InvalidMetricException, InvalidDateRangeException {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        double average = 10.0;
        String metric = "temperature";
        when(sensorReadingDao.getAverageMetricValue(metric, startDate, endDate)).thenReturn(average);
        ResponseEntity<?> response = controller.getAverageMetricValue(startDate, endDate, metric);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(average, response.getBody());
    }

    @Test
    public void getAverageMetricValueForAllSensorsShouldReturnBadRequest() throws InvalidMetricException, InvalidDateRangeException {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        String metric = "invalid_metric";
        when(sensorReadingDao.getAverageMetricValue(metric, startDate, endDate)).thenThrow(new InvalidMetricException());
        ResponseEntity<?> response = controller.getAverageMetricValue(startDate, endDate, metric);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(INVALID_METRIC_MESSAGE, response.getBody());
    }

    @Test
    public void getAverageMetricValueForSensorShouldReturnOk() throws InvalidMetricException, InvalidDateRangeException {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        double average = 10.0;
        String sensorId = "sensor1";
        String metric = "temperature";
        when(sensorReadingDao.getAverageMetricValueBySensor(sensorId, metric, startDate, endDate)).thenReturn(average);
        ResponseEntity<?> response = controller.getAverageMetricValueForSensor(sensorId, metric, startDate, endDate);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(average, response.getBody());
    }

    @Test
    public void getAverageMetricValueForSensorShouldReturnBadRequest() throws InvalidMetricException, InvalidDateRangeException {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now().plusHours(1);
        String sensorId = "sensor1";
        String metric = "invalid_metric";
        when(sensorReadingDao.getAverageMetricValueBySensor(sensorId, metric, startDate, endDate)).thenThrow(new InvalidMetricException());
        ResponseEntity<?> response = controller.getAverageMetricValueForSensor(sensorId, metric, startDate, endDate);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(INVALID_METRIC_MESSAGE, response.getBody());
    }
}
