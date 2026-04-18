/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class DiscoveryResource {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiInfo(){
        
        // Build the response as a Map
        Map<String, Object> apiInfo = new HashMap<>();
        
        apiInfo.put("name", "Smart Campus Sensor & Room Management API");
        apiInfo.put("version", "1.0");
        apiInfo.put("contact", "admin@smartcampus.ac.uk");
        apiInfo.put("description", "API for managing campus rooms and IoT sensors");
        
        // Provide links to available resources
        Map<String, String> resources = new HashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        apiInfo.put("resources", resources);
        
        return Response.ok(apiInfo).build();
    }
}
