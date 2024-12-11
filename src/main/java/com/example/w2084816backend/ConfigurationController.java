package com.example.w2084816backend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

/**
 * Controller class for handling API requests related to system configurations.
 * Provides endpoints for retrieving and saving configuration details.
 */

@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController {
    // Autowired service to interact with the configuration data
    @Autowired
    private ConfigurationService configurationService;

    // Endpoint to retrieve the latest configuration
    @GetMapping
    public Configuration getLatestConfiguration() {
        return configurationService.getLatestConfiguration();
    }

    // Endpoint to save or update configuration
    @PostMapping
    public Configuration saveConfiguration(@RequestBody Configuration configuration) {
        // Set the current timestamp when saving the configuration
        configuration.setCreatedAt(LocalDateTime.now());
        // Call the service to save the configuration
        return configurationService.saveConfiguration(configuration);
    }
}