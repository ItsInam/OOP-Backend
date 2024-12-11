package com.example.w2084816backend;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * VendorEntity class represents a vendor in the database.
 * It contains the vendor's ID, name, the number of tickets produced, and the creation time.
 */

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

}
