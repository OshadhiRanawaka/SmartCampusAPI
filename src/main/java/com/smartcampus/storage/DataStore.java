/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.storage;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {
    
    // Static maps act as our in-memory database
    public static final Map<String, Room> rooms = new HashMap<>();
    public static final Map<String, Sensor> sensors = new HashMap<>();
    public static final Map<String, List<SensorReading>> readings = new HashMap<>();
    
    // Pre-load some sample data when the application starts
    static {
        // Sample Rooms
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        Room r2 = new Room("LAB-101", "Computer Lab", 30);
        rooms.put(r1.getId(), r1);
        rooms.put(r2.getId(), r2);
        
        // Sample Sensors
        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor s2 = new Sensor("CO2-001", "CO2", "ACTIVE", 400.0, "LAB-101");
        Sensor s3 = new Sensor("OCC-001", "Occupancy", "MAINTENANCE", 0.0, "LIB-301");
        sensors.put(s1.getId(), s1);
        sensors.put(s2.getId(), s2);
        sensors.put(s3.getId(), s3);
        
        // Link sensors to their rooms
        r1.getSensorIds().add("TEMP-001");
        r1.getSensorIds().add("OCC-001");
        r2.getSensorIds().add("CO2-001");
        
        // Sample Readings
        List<SensorReading> readingsForTemp = new ArrayList<>();
        readingsForTemp.add(new SensorReading(21.0));
        readingsForTemp.add(new SensorReading(22.5));
        readings.put("TEMP-001", readingsForTemp);

        List<SensorReading> readingsForCO2 = new ArrayList<>();
        readingsForCO2.add(new SensorReading(398.0));
        readingsForCO2.add(new SensorReading(400.0));
        readings.put("CO2-001", readingsForCO2);
        
        // OCC-001 starts with empty readings (MAINTENANCE sensor)
        readings.put("OCC-001", new ArrayList<>());
    }
}
