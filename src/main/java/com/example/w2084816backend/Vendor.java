package com.example.w2084816backend;
import java.sql.*;
import java.time.LocalDateTime;

/**
 * Vendor class represents a vendor who generates tickets.
 * Implements the Runnable interface to allow for multi-threaded execution.
 * Each vendor creates a specific number of tickets and adds them to the ticket pool.
 */

public class Vendor implements Runnable {
    // Initialize vendor attributes
    private final TicketPool ticketPool;
    private final int releaseRate;
    private final int vendorID;
    private final String name;
    private final Configuration configuration;
    private final TicketRepository ticketRepository;
    private int ticketsProduced=0;
    static int ticketNumber = 0;

    // Constructor to initialize Vendor object
    public Vendor(TicketPool ticketPool, int releaseRate, int vendorID, String name, Configuration configuration, TicketRepository ticketRepository) {
        this.ticketPool = ticketPool;
        this.releaseRate = releaseRate;
        this.vendorID = vendorID;
        this.name = name;
        this.configuration = configuration;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void run() {
        try {
            // Ensure the vendor stops adding tickets when the total tickets are reached
            while (ticketPool.getTicketsAdded() < configuration.getTotalTickets()) {
                String ticketCode = "Ticket-" + "v" + vendorID + "-" + (++ticketNumber);
                boolean success = ticketPool.addTicket(this, ticketCode, vendorID, configuration.getTotalTickets());
                if (!success) {
                    break; // Stop if global ticket limit is reached
                }
                ticketsProduced++;// Increment the ticket counter for the vendor
                // Updates the number of tickets produced by vendor in the database
                try (Connection connection = Configuration.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement("UPDATE vendor_entity SET tickets_produced = ? WHERE id = ?")) {
                    preparedStatement.setInt(1, ticketsProduced);
                    preparedStatement.setInt(2, vendorID);
                    preparedStatement.executeUpdate();
                }
                Thread.sleep(1000 / releaseRate); // Control ticket release rate
            }
            System.out.println("Vendor "+name + " has finished adding tickets.");
        } catch (InterruptedException | SQLException e) {
            System.out.println("Vendor "+name + " interrupted: " + e.getMessage());
        }
    }

    // Methods to add new vendor to the database
    public static void addVendor(int id, LocalDateTime time, String name) throws SQLException {
        try (Connection connection = Configuration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO vendor_entity(id,created_at,name) VALUES (?,?,?)")) {
            preparedStatement.setInt(1, id);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(time));
            preparedStatement.setString(3, name);
            if(preparedStatement.executeUpdate()!=0){

            }
            else {
                throw new SQLException("Vendor ID not generated.");
            }
        }
    }
    // Getters
    public String getName() {
        return name;
    }
}
