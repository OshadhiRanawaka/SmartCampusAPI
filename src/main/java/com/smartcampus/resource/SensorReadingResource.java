/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.storage.DataStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
    
    // The sensorId is passed in from SensorResource's sub-resource locator
    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }
    
    // GET /api/v1/sensors/{sensorId}/readings
    // Returns the full reading history for this sensor
     @GET
    public Response getReadings(){
        
        List<SensorReading> readingList = DataStore.readings.get(sensorId);

        // If no readings list exists yet, return an empty array
        if (readingList == null) {
            readingList = new ArrayList<>();
        }

        Sensor sensor = DataStore.sensors.get(sensorId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("sensorId", sensorId);
        response.put("sensorType", sensor.getType());
        response.put("currentValue", sensor.getCurrentValue());
        response.put("totalReadings", readingList.size());
        response.put("readings", readingList);

        return Response.ok(response).build();
    }
    
    // POST /api/v1/sensors/{sensorId}/readings
    // Adds a new reading for this sensor
    // Also updates the sensor's currentValue (side effect)
    // Blocked if sensor status is MAINTENANCE
    @POST
    public Response addReading(SensorReading reading){
        
        // Get the parent sensor
        Sensor sensor = DataStore.sensors.get(sensorId);
        
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId, sensor.getStatus());
        }
        
        if ("OFFLINE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId, sensor.getStatus());
        }
        
        // --- Validate the reading value ---
        if (reading == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("error", "Bad Request");
            error.put("message", "Reading body cannot be empty.");
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(error).build();
        }
        
        // --- Create a proper reading with auto-generated ID and timestamp ---
        SensorReading newReading = new SensorReading(reading.getValue());

        // --- Save the reading to the history list ---
        List<SensorReading> readingList = DataStore.readings.get(sensorId);
        if (readingList == null) {
            readingList = new ArrayList<>();
            DataStore.readings.put(sensorId, readingList);
        }
        readingList.add(newReading);
        
        // Update the sensor's currentValue ---
        // This keeps the sensor's latest reading always up to date
        sensor.setCurrentValue(newReading.getValue());

        // Build a confirmation response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reading added successfully.");
        response.put("sensorId", sensorId);
        response.put("recordedValue", newReading.getValue());
        response.put("updatedCurrentValue", sensor.getCurrentValue());
        response.put("reading", newReading);

        return Response.status(Response.Status.CREATED)
                       .entity(response).build();
    }
}
