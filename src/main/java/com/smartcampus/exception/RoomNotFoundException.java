/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception;


public class RoomNotFoundException extends RuntimeException{
    
    public RoomNotFoundException(String roomId){
        super("Room with ID '" + roomId + "' was not found.");
    } 
    
}
