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
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketCode;
    private String status; // 'available' or 'sold'
    private Integer vendorId;
    private Integer customerId;
    private LocalDateTime createdAt;
    private LocalDateTime soldAt;
}
