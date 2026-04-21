/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.model;

import java.time.Instant;

public class ErrorResponse {
    
    private int status;
    private String error;
    private String message;
    private String hint;
    private String timestamp;
    private String documentation;
    
    // Default constructor
    public ErrorResponse() {}
    
    // Constructor for Simple errors
    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = Instant.now().toString();
        this.documentation = "http://localhost:8080/SmartCampusAPI/api/v1";
    }
    
    //Constructor for errors with hint
    public ErrorResponse(int status, String error, String message, String hint) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.hint = hint;
        this.timestamp = Instant.now().toString();
        this.documentation = "http://localhost:8080/SmartCampusAPI/api/v1";
    }

    //Getters
    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getHint() {
        return hint;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDocumentation() {
        return documentation;
    }

    //Setters
    public void setStatus(int status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    
}
