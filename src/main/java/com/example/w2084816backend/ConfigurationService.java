package com.example.w2084816backend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for handling configuration-related logic.
 * Provides methods for retrieving and saving configuration data.
 */

@Service
public class ConfigurationService {
    @Autowired
    private ConfigurationRepository configurationRepository;
    // Retrieve the latest configuration from the databse
    public Configuration getLatestConfiguration() {
        return configurationRepository.findFirstByOrderByIdDesc();
    }

    // Save a new configuration or updates an existing one in the database
    public Configuration saveConfiguration(Configuration configuration) {
        return configurationRepository.save(configuration);
    }
}
