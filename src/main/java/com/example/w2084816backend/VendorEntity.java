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
public class VendorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int ticketsProduced = 0;

    private LocalDateTime createdAt;

    public void incrementTicketsProduced() {
        this.ticketsProduced++;
    }
}
