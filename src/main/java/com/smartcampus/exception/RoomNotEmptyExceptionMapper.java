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
public class RoomNotEmptyExceptionMapper implements ExceptionMapper<RoomNotEmptyException> {
    
    @Override
    public Response toResponse(RoomNotEmptyException exception){
        
        ErrorResponse error = new ErrorResponse(
            409,
            "Conflict",
            exception.getMessage(),
            "Remove or reassign all sensors from this room before attempting deletion. " +
            "Use GET /api/v1/rooms/{roomId} to see which sensors are assigned."
        );
        
        return Response
                .status(Response.Status.CONFLICT) // 409
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
}
