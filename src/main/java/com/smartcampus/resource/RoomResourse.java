/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotFoundException;
import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.Room;
import com.smartcampus.storage.DataStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResourse {
    
    // GET /api/v1/rooms
    // Returns a list of ALL rooms in the system
    @GET
    public Response getAllRooms(){
        List<Room> roomList = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(roomList).build();
    }
    
    // POST /api/v1/rooms
    // Creates a new room from the JSON body sent by the client
    @POST
    public Response creatRoom(Room room){
        
        //Check that id is not empty
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            Map<String, Object> error = new java.util.HashMap<>();
            error.put("status", 400);
            error.put("error", "Bad Request");
            error.put("message", "Room ID cannot be empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        
        //Check that name is not empty
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            Map<String, Object> error = new java.util.HashMap<>();
            error.put("status", 400);
            error.put("error", "Bad Request");
            error.put("message", "Room name cannot be empty.");
            return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
        }
        
        // Check if a room with same ID already exists
        if (DataStore.rooms.containsKey(room.getId())) {
            Map<String, Object> error = new java.util.HashMap<>();
            error.put("status", 409);
            error.put("error", "Conflict");
            error.put("message", "A room with ID '" + room.getId() + "' already exists.");
            return Response.status(Response.Status.CONFLICT).entity(error).build();
        }
        
        //Save the new room to in-memeory store
        DataStore.rooms.put(room.getId(), room);
        
        // Return 201 Created with the new room in the body
        return Response.status(Response.Status.CREATED).entity(room).build();
    }
    
    // GET /api/v1/rooms/{roomId}
    // Returns ONE specific room by its ID
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId){
        Room room = DataStore.rooms.get(roomId);
        
        // If room not found, throw custom exception
        // The RoomNotFoundExceptionMapper will catch this and return a 404
        if (room == null) {
            throw new RoomNotFoundException(roomId);
        }

        return Response.ok(room).build(); // 200 OK
    }
    
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId){
        
        //Chaeck if room exists 
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            throw new RoomNotFoundException(roomId);
        }
        
        //If room still has sensors prevent deletion
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
            // The RoomNotEmptyExceptionMapper catches this and returns 409 Conflict
        }
        
        DataStore.rooms.remove(roomId);
        
        // Return 200 OK with a confirmation message
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("message", "Room '" + roomId + "' has been successfully deleted.");
        response.put("deletedRoomId", roomId);

        return Response.ok(response).build();
    }
    
}
