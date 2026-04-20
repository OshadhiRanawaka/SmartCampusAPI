/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;


public class SensorNotFoundException extends RuntimeException {
    
    public SensorNotFoundException(String sensorId) {
        super("Sensor with ID '" + sensorId + "' was not found.");
    }
}
