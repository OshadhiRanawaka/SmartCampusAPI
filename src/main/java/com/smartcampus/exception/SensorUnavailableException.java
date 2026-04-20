/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;


public class SensorUnavailableException extends RuntimeException {
    
    public SensorUnavailableException(String sensorId, String status) {
        super("Sensor '" + sensorId + "' cannot accept readings. " +
              "Current status is: " + status + ". " +
              "Only ACTIVE sensors can receive new readings.");
    }
}
