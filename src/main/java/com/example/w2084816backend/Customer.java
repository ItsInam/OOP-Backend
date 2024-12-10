package com.example.w2084816backend;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate;
    private final int customerID;
    private final String name;
    private final String type;
    public final Configuration configuration;
    private int ticketsPurchased=0;

    public Customer(TicketPool ticketPool, int retrievalRate, int customerID, String name, String type, Configuration configuration) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
        this.customerID = customerID;
        this.name = name;
        this.type = type;
        this.configuration = configuration;
    }

    @Override
    public void run() {
        try {
            while (ticketPool.getTicketsSold() < configuration.getTotalTickets()) {
                ticketPool.retrieveTicket(this);
                ticketsPurchased++;
                try (Connection connection = Configuration.getConnection();
                     PreparedStatement preparedStatement = connection.prepareStatement("UPDATE customer_entity SET tickets_purchased = ? WHERE id = ?")) {
                    preparedStatement.setInt(1, ticketsPurchased);
                    preparedStatement.setInt(2, customerID);
                    preparedStatement.executeUpdate();
                }
                Thread.sleep(1000 / retrievalRate); // Control ticket retrieval rate
            }
            System.out.println(type+ " Customer "+name + " cannot buy more tickets. All tickets are sold!");
        } catch (InterruptedException | SQLException e) {
            System.out.println(type+ " Customer "+name + " interrupted: " + e.getMessage());
        }
    }

    public static void addCustomer(int id, LocalDateTime time, String name,String type) throws SQLException {
        try (Connection connection = Configuration.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO customer_entity(id,created_at,name,type) VALUES (?,?,?,?)")) {
            preparedStatement.setInt(1, id);
            preparedStatement.setTimestamp(2, Timestamp.valueOf(time));
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, type);
            if(preparedStatement.executeUpdate()!=0){

            }
            else {
                throw new SQLException("Vendor ID not generated.");
            }
        }
    }

    public String getType() {
        return type;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getName() {
        return name;
    }
}

