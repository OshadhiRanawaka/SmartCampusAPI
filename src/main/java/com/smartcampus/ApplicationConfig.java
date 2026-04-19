/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus;

import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        // This tells Jersey to scan ALL sub-packages under com.smartcampus
        packages("com.smartcampus");
    }
}