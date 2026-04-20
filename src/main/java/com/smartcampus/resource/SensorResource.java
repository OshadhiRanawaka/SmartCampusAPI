/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.exception.SensorNotFoundException;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {
    
    // GET /api/v1/sensors
    // GET /api/v1/sensors?type=CO2
    // Returns ALL sensors, or filters by type if provided
    @GET
    public Response getSensors(@QueryParam("type") String type){
        
        List<Sensor> sensorList = new ArrayList<>(DataStore.sensors.values());
        
        if (type != null && !type.trim().isEmpty()) {
            List<Sensor> filtered = new ArrayList<>();
            for (Sensor s : sensorList) {
                // Case-insensitive comparison for better usability
                if (s.getType().equalsIgnoreCase(type.trim())) {
                    filtered.add(s);
                }
            }
            // Return filtered list even if empty - that's valid
            return Response.ok(filtered).build();
        }
        
        // No filter - return all sensors
        return Response.ok(sensorList).build();
    }
    
    // POST /api/v1/sensors
    // Registers a new sensor
    // Validates that the roomId in the body exists
    @POST
    public Response createSensor(Sensor sensor){
        
        // --- Validation 1: Check required fields ---
        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("error", "Bad Request");
            error.put("message", "Sensor ID cannot be empty.");
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(error).build();
        }
        
        if (sensor.getType() == null || sensor.getType().trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("error", "Bad Request");
            error.put("message", "Sensor type cannot be empty.");
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(error).build();
        }
        
        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("error", "Bad Request");
            error.put("message", "roomId cannot be empty. A sensor must be assigned to a room.");
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(error).build();
        }
        
        // --- Validation 2: Check sensor ID is not already taken ---
        if (DataStore.sensors.containsKey(sensor.getId())) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 409);
            error.put("error", "Conflict");
            error.put("message", "A sensor with ID '" + sensor.getId() + "' already exists.");
            return Response.status(Response.Status.CONFLICT)
                           .entity(error).build();
        }
        
        // --- Validation 3: Check the roomId actually exists ---
        if (!DataStore.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "The roomId '" + sensor.getRoomId() + "' does not exist. " +
                "A sensor must be linked to a valid room."
            );
        }
        
        // --- Set default status if not provided ---
        if (sensor.getStatus() == null || sensor.getStatus().trim().isEmpty()) {
            sensor.setStatus("ACTIVE");
        }
        
        // --- Save the new sensor ---
        DataStore.sensors.put(sensor.getId(), sensor);

        //Add this sensor's ID to the Room's sensorIds list ---
        DataStore.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());
        
        // --- Initialise an empty readings list for this sensor ---
        DataStore.readings.put(sensor.getId(), new ArrayList<SensorReading>());
        
        return Response.status(Response.Status.CREATED)
                       .entity(sensor).build();
    }
    
    // GET /api/v1/sensors/{sensorId}
    // Returns ONE specific sensor by its ID
     @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId){
        
        Sensor sensor = DataStore.sensors.get(sensorId);

        if (sensor == null) {
            throw new SensorNotFoundException(sensorId);
        }

        return Response.ok(sensor).build();
    }
    
    // GET  /api/v1/sensors/{sensorId}/readings
    // POST /api/v1/sensors/{sensorId}/readings
    // Locates and returns the SensorReadingResource instance
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(
            @PathParam("sensorId") String sensorId) {

        // First verify the sensor actually exists
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new SensorNotFoundException(sensorId);
        }

        // Pass the sensorId to the sub-resource class
        return new SensorReadingResource(sensorId);
    }
}
