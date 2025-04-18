package com.example.w2084816backend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SimulationService manages the simulation's lifecycle, including starting and stopping the simulation.
 * It handles the creation of vendors and customers, the addition of tickets, and communication with the database.
 */

@Service
public class SimulationService {
    // Initializing simulation service attributes
    @Autowired
    private TicketRepository ticketRepository;
    private boolean isSimulationRunning = false;
    private ExecutorService executorService;
    private TicketPool ticketPool;
    private Configuration configuration;

    // Start simulation with given configuration
    public void startSimulation(Configuration configuration) throws SQLException {
        if (isSimulationRunning) {
            throw new IllegalStateException("Simulation is already running!");
        }
        this.configuration = configuration;
        this.isSimulationRunning = true;
        this.ticketPool = new TicketPool(configuration.getMaxTicketCapacity());// Initialize the ticket pool
        this.executorService = Executors.newCachedThreadPool();// Create a thread pool to manage vendors and customers
        System.out.println("Simulation started!");
        clearDatabase();// Clear any previous data from the database

        // Create and start vendor threads
        executorService.submit(new Thread(new Vendor(ticketPool, configuration.getTicketReleaseRate(),1, "Ivan", configuration,ticketRepository)));
        executorService.submit(new Thread(new Vendor(ticketPool, configuration.getTicketReleaseRate(),2, "Steven", configuration,ticketRepository)));

        // Create and start customer threads
        Customer c1=new Customer(ticketPool, configuration.getCustomerRetrievalRate(),1, "Alice", "VIP", configuration);
        ticketPool.addCustomerToQueue(c1);
        executorService.submit(new Thread(c1));

        Customer c2=new Customer(ticketPool, configuration.getCustomerRetrievalRate(),2, "Moiraine", "VIP", configuration);
        ticketPool.addCustomerToQueue(c2);
        executorService.submit(new Thread(c2));

        Customer c3=new Customer(ticketPool, configuration.getCustomerRetrievalRate(),3, "Rand", "Regular", configuration);
        ticketPool.addCustomerToQueue(c3);
        executorService.submit(new Thread(c3));

        Customer c4=new Customer(ticketPool, configuration.getCustomerRetrievalRate(),4, "Ash", "Regular", configuration);
        ticketPool.addCustomerToQueue(c4);
        executorService.submit(new Thread(c4));

        // Add vendors and customers to the database
        Vendor.addVendor(1,LocalDateTime.now(),"Ivan");
        Vendor.addVendor(2,LocalDateTime.now(),"Steven");
        Customer.addCustomer(1,LocalDateTime.now(),"Alice", "VIP");
        Customer.addCustomer(2,LocalDateTime.now(),"Moiraine", "VIP");
        Customer.addCustomer(3,LocalDateTime.now(),"Rand", "Regular");
        Customer.addCustomer(4,LocalDateTime.now(),"Ash", "Regular");
    }
    // Stops simulation if it is running
    public void stopSimulation() {
        if (!isSimulationRunning) {
            throw new IllegalStateException("No simulation is currently running!");
        }
        System.out.println("Stopping simulation...");
        isSimulationRunning = false;
        executorService.shutdownNow(); // Stop all threads
        System.out.println("Simulation stopped!");
    }
    // Clear all data from the database
    public static void clearDatabase(){
        try(Connection connection=Configuration.getConnection()){
            try (PreparedStatement truncateTickets = connection.prepareStatement("TRUNCATE TABLE ticket")) {
                truncateTickets.executeUpdate();
            }
            try (PreparedStatement truncateCustomers = connection.prepareStatement("TRUNCATE TABLE customer_entity")) {
                truncateCustomers.executeUpdate();
            }
            try (PreparedStatement truncateVendors = connection.prepareStatement("TRUNCATE TABLE vendor_entity")) {
                truncateVendors.executeUpdate();
            }
            try (PreparedStatement truncateVendors = connection.prepareStatement("TRUNCATE TABLE configuration")) {
                truncateVendors.executeUpdate();
            }
        }
        catch (SQLException e){
            System.out.println("Unexpected error clearing database - "+e.getMessage());
            e.printStackTrace();
        }
    }
}