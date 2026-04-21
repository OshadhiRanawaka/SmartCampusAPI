/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

import com.smartcampus.model.ErrorResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SensorNotFoundExceptionMapper implements ExceptionMapper<SensorNotFoundException> {
    
    @Override
    public Response toResponse(SensorNotFoundException exception){
        
        ErrorResponse error = new ErrorResponse(
            404,
            "Not Found",
            exception.getMessage(),
            "Use GET /api/v1/sensors to see all available sensor IDs."
        );

        return Response
                .status(Response.Status.NOT_FOUND) // 404
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
}
