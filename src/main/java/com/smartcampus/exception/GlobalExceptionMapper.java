/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    // Logger writes full details server-side for admins to see
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {

        // Log the FULL stack trace internally on the server
        LOGGER.log(Level.SEVERE, "Unexpected server error: ", exception);

        
        Map<String, Object> error = new HashMap<>();
        error.put("status", 500);
        error.put("error", "Internal Server Error");
        error.put("message", "An unexpected error occurred on the server. " +
                             "Please contact the system administrator.");
        error.put("documentation", "http://localhost:8080/SmartCampusAPI/api/v1");

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR) // 500
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}