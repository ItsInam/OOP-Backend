package com.example.w2084816backend;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * TicketRepository is an interface that extends JpaRepository.
 * This interface provides methods for CRUD operations on the Ticket entity.
 * Spring Data JPA will automatically generate the implementation of this interface.
 */

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
