package com.example.w2084816backend;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The TicketPool class manages the ticket pool where tickets are added by vendors and retrieved by customers.
 * It also ensures that the number of tickets does not exceed the maximum allowed capacity and that customers
 * can retrieve tickets in an orderly fashion (VIP customers first).
 */

public class TicketPool {
    // Initialize ticketPool attributes
    private final int maxCapacity;
    private final BlockingQueue<String> ticketPool;
    private final Queue<Customer> customerQueue = new LinkedList<>();
    private static int ticketsAdded = 0;
    private static int ticketsSold = 0;

    // Constructor initializes the ticket pool with a given maximum capacity
    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.ticketPool = new LinkedBlockingQueue<>(maxCapacity);
    }

    // Add ticket to ticketPool
    public synchronized boolean addTicket(Vendor vendor, String ticket, int vendorID, int totalTicketsAllowed) throws InterruptedException, SQLException {
        // If the total number of tickets added exceeds the allowed limit, return false
        if (ticketsAdded >= totalTicketsAllowed) {
            return false;
        }
        ticketPool.put(ticket);// Add ticket to pool
        ticketsAdded++;// Increment tickets added
        System.out.println("Ticket added by " + vendor.getName() + ": " + ticket);
        notifyAll();// Notify all threads that a ticket is available

        // Insert the ticket information into the database
        try (Connection connection = Configuration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO ticket (id,created_at,status,ticket_code,vendor_id) VALUES (?,?, 'available', ?,?)")) {
            preparedStatement.setInt(1, ticketsAdded);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(3,ticket);
            preparedStatement.setInt(4,vendorID);
            preparedStatement.executeUpdate();
        }
        return true;
    }

    // Retrieve a ticket from the pool for the customer
    public synchronized void retrieveTicket(Customer customer) throws InterruptedException, SQLException {
        // Wait until the customer can retrieve a ticket
        while (ticketPool.isEmpty() || !customerQueue.peek().equals(customer)) {
            wait();// Wait until it's this customer's turn and a ticket is available
        }

        String ticket = ticketPool.poll();// Retrieve ticket from pool
        if (ticket != null) {
            ticketsSold++;// Increment the tickets sold counter
            System.out.println("Ticket retrieved by "+customer.getType()+" Customer " + customer.getName() + ": " + ticket);
        }
        // Move the customer back to the queue
        customerQueue.poll();
        customerQueue.offer(customer);
        notifyAll();// Notify other customers that a ticket is available
        // Update ticket status in the database
        try (Connection connection = Configuration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE ticket SET status = 'sold', customer_id = ?, sold_at = NOW() WHERE ticket_code = ?")) {
            preparedStatement.setInt(1, customer.getCustomerID());
            preparedStatement.setString(2, ticket);
            preparedStatement.executeUpdate();

        }
    }

    // Add customer to customer queue
    public synchronized void addCustomerToQueue(Customer customer) {
        customerQueue.offer(customer);
    }

    // Gets the total number of tickets sold
    public synchronized int getTicketsSold() {
        return ticketsSold;
    }

    // Gets the total number of tickets added
    public synchronized int getTicketsAdded() {
        return ticketsAdded;
    }
}
