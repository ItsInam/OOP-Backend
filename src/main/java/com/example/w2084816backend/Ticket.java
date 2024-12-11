package com.example.w2084816backend;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * The Ticket class represents a ticket in the system.
 * It is mapped to a database table where each ticket has a unique ticket code, status, and associated vendor and customer IDs.
 * This class includes Lombok annotations to automatically generate getters, setters, constructors, and other boilerplate code.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    // Initializing the attributes of ticket
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ticketCode;
    private String status;
    private Integer vendorId;
    private Integer customerId;
    private LocalDateTime createdAt;
    private LocalDateTime soldAt;
}
