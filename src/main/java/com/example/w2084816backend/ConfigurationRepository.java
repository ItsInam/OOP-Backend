package com.example.w2084816backend;



import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    Configuration findFirstByOrderByIdDesc(); // Get the latest configuration
}
