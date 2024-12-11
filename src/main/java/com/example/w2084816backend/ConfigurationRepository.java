package com.example.w2084816backend;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository interface for accessing configuration data from the database.
 * Extends JpaRepository to leverage built-in CRUD operations for the Configuration entity.
 */
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    // Retrive the latest configuration from the databse
    Configuration findFirstByOrderByIdDesc(); // Get the latest configuration
}
