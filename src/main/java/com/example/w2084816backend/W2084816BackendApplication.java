package com.example.w2084816backend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Spring Boot application.
 * This class starts the Spring Boot application and initializes the simulation backend.
 */

@SpringBootApplication
public class W2084816BackendApplication {
    // Main method which launches the spring boot application
    public static void main(String[] args) {
        // Runs the Spring Boot application
        SpringApplication.run(W2084816BackendApplication.class, args);
        System.out.println("Simulation backend is running...");
    }

}
