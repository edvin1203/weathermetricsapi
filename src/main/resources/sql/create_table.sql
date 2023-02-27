CREATE TABLE weather_sensor_readings (
  metric_type VARCHAR(50) NOT NULL CHECK (metric_type IN ('humidity', 'windspeed', 'temperature')),
  metric_value FLOAT NOT NULL,
  reading_date DATETIME NOT NULL,
  sensor_id VARCHAR(50) NOT NULL,
  PRIMARY KEY (reading_date, sensor_id),
  INDEX (reading_date),
  INDEX (sensor_id)
);