/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RoomNotFoundExceptionMapper implements ExceptionMapper<RoomNotFoundException>{
    
    @Override
    public Response toResponse(RoomNotFoundException exception){
        
        Map<String, Object> error = new HashMap<>();
        error.put("status", 404);
        error.put("error", "Not Found");
        error.put("message", exception.getMessage());

        return Response
                .status(Response.Status.NOT_FOUND) // 404
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
