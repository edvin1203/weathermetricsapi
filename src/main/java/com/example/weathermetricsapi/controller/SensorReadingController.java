package com.example.weathermetricsapi.controller;

import com.example.weathermetricsapi.dao.SensorReadingDao;
import com.example.weathermetricsapi.exceptions.InvalidDateRangeException;
import com.example.weathermetricsapi.exceptions.InvalidMetricException;
import com.example.weathermetricsapi.model.SensorReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
public class SensorReadingController {

    @Autowired
    private SensorReadingDao sensorReadingDao;

    @PostMapping("/sensor-reading")
    public ResponseEntity<?> createSensorReading(@RequestBody SensorReading sensorReading) {
        try {
            sensorReadingDao.insertSensorReading(sensorReading);
            return ResponseEntity.ok().build();
        } catch (InvalidMetricException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/average-metric-value/{metric}")
    public ResponseEntity<?> getAverageMetricValueForAllSensors(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDateTime startDate,
                                                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDateTime endDate,
                                                                @PathVariable String metric) {
        try {
            double average = sensorReadingDao.getAverageMetricValue(metric, startDate, endDate);
            return ResponseEntity.ok().body(average);
        } catch (InvalidMetricException | InvalidDateRangeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/average-metric-value/{metric}/{sensorId}")
    public ResponseEntity<?> getAverageMetricValueForSensor(@PathVariable String sensorId,
                                                            @PathVariable String metric,
                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDateTime startDate,
                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDateTime endDate) {
        try {
            double average = sensorReadingDao.getAverageMetricValueBySensor(sensorId, metric, startDate, endDate);
            return ResponseEntity.ok().body(average);
        } catch (InvalidMetricException | InvalidDateRangeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


}
