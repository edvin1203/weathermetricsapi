# Weather Metrics API
This is a simple API for retrieving and creating sensor readings for weather metrics, such as temperature and humidity. The API is built with Spring Boot and uses a MySQL database to store sensor readings.

# Endpoints
POST /sensor-reading
This endpoint lets you send in sensor readings and store them in the database. The request body should include a SensorReading object with the following properties:

sensorId: The ID of the sensor that generated the reading.
metricType: The name of the metric that was measured.(available metric types: "temperature", "humidity", "windspeed")
metricValue: The value of the metric that was measured (e.g. 31.5).
readingDate: The date when the reading was taken (e.g. 2022-02-26T15:00:06).
If the metric name is invalid, the server returns a 400 Bad Request response with an error message.
Example request body:

`{
    "sensorId": "sensor2",
    "readingDate": "2022-02-26T15:00:06",
    "metricValue": 30.5,
    "metricType": "temperature"
}`

## GET /average-metric-value/{metric}
This endpoint retrieves the average value of a weather metric for a given time range. The endpoint takes two query parameters:

startDate: The start of the time range, e.g. 2022-02-26T15:00:06.
endDate: The end of the time range, e.g. 2023-02-26T15:00:06.
If the metric name or time range is invalid, the server returns a 400 Bad Request response with an error message.

## GET /average-metric-value/{metric}/{sensorId}
This endpoint retrieves the average value of a weather metric for a given sensor and time range. The endpoint takes two query parameters:

startDate: The start of the time range.
endDate: The end of the time range.
If the metric name, sensor ID, or time range is invalid, the server returns a 400 Bad Request response with an error message.

## Dependencies
This API uses the following dependencies:
Spring Boot: for building and running the application
MySQL: for storing sensor readings in a relational database

## Table creation: 
The table creation SQL can be found here: https://github.com/edvin1203/weathermetricsapi/blob/master/src/main/resources/sql/create_table.sql

# License
This project is licensed under the MIT License. Feel free to use, modify, and distribute the code as you see fit.