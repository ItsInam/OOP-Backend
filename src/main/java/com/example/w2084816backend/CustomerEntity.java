package com.example.w2084816backend;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
        * CustomerEntity class represents a customer in the system.
        * It stores the customer information such as name, type (VIP or Regular),
        * number of tickets purchased, and the creation timestamp.
 * This entity is persisted in the database.
 */

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private int ticketsPurchased = 0;
    private LocalDateTime createdAt;

}
