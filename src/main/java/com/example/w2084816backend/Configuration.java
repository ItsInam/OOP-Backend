package com.example.w2084816backend;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Entity class for configuration details.
 * Represents a database table for storing system configurations.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Initializing configuration attributes
    private Long id;
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private LocalDateTime createdAt;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ticketSystem";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Getters
    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

   // Setters
    public void setCreatedAt(LocalDateTime time) {
        this.createdAt=time;
    }

    // Establish a connection with the database
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
