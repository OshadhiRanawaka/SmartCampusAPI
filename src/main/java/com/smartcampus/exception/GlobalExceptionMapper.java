/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

import com.smartcampus.model.ErrorResponse;
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

        ErrorResponse error = new ErrorResponse(
            500,
            "Internal Server Error",
            "An unexpected error occurred on the server. " +
            "Please contact the system administrator.",
            "If this issue persists, reference the server logs with timestamp: " +
            java.time.Instant.now().toString()
        );
        
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR) // 500
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}