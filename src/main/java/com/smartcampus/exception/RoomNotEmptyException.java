/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;

public class RoomNotEmptyException extends RuntimeException{
    
    private String roomId;
    
    public RoomNotEmptyException(String roomId){
        super("Cannot delete room '" + roomId + "'. Active sensors are still asssigned to it.");
        this.roomId = roomId;
    }
    
    public String getRoomId(){
        return roomId;
    }
}
