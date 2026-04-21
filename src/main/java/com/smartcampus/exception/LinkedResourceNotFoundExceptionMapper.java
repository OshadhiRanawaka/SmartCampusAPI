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
public class LinkedResourceNotFoundExceptionMapper implements ExceptionMapper<LinkedResourceNotFoundException> {
    
    @Override
    public Response toResponse(LinkedResourceNotFoundException exception){
        
        ErrorResponse error = new ErrorResponse(
            422,
            "Unprocessable Entity",
            exception.getMessage(),
            "Create the room first using POST /api/v1/rooms, " +
            "then register the sensor with that room's ID."
        );
        
        // 422 Unprocessable Entity
        return Response
                .status(422)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
