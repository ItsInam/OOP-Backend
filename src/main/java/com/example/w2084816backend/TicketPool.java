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

public class TicketPool {
    private final int maxCapacity;
    private final BlockingQueue<String> ticketPool;
    private final Queue<Customer> customerQueue = new LinkedList<>();
    private static int ticketsAdded = 0;
    private static int ticketsSold = 0;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.ticketPool = new LinkedBlockingQueue<>(maxCapacity);
    }

    public synchronized boolean addTicket(Vendor vendor, String ticket, int vendorID, int totalTicketsAllowed) throws InterruptedException, SQLException {
        if (ticketsAdded >= totalTicketsAllowed) {
            return false; // Stop adding tickets once the global limit is reached
        }
        ticketPool.put(ticket);
        ticketsAdded++;
        System.out.println("Ticket added by " + vendor.getName() + ": " + ticket);
        notifyAll(); // Notify waiting customers that a ticket is available
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

    public synchronized void retrieveTicket(Customer customer) throws InterruptedException, SQLException {
        while (ticketPool.isEmpty() || !customerQueue.peek().equals(customer)) {
            wait(); // Wait until it's this customer's turn and a ticket is available
        }

        String ticket = ticketPool.poll();
        if (ticket != null) {
            ticketsSold++;
            System.out.println("Ticket retrieved by "+customer.getType()+" Customer " + customer.getName() + ": " + ticket);
        }
        customerQueue.poll(); // Remove the current customer from the queue
        customerQueue.offer(customer); // Add the customer back to the queue for their next turn
        notifyAll(); // Notify the next customer that it's their turn
        try (Connection connection = Configuration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE ticket SET status = 'sold', customer_id = ?, sold_at = NOW() WHERE ticket_code = ?")) {
            preparedStatement.setInt(1, customer.getCustomerID());
            preparedStatement.setString(2, ticket);
            preparedStatement.executeUpdate();

        }
    }

    public synchronized void addCustomerToQueue(Customer customer) {
        customerQueue.offer(customer);
    }

    public synchronized int getTicketsSold() {
        return ticketsSold;
    }

    public synchronized int getTicketsAdded() {
        return ticketsAdded;
    }
}
