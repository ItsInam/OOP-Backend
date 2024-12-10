package com.example.w2084816backend;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type; // VIP or Regular
    private int ticketsPurchased = 0;

    private LocalDateTime createdAt;

    public void incrementTicketsPurchased() {
        this.ticketsPurchased++;
    }
}
