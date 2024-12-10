package com.example.w2084816backend;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/configuration")
public class ConfigurationController {
    @Autowired
    private ConfigurationService configurationService;

    @GetMapping
    public Configuration getLatestConfiguration() {
        return configurationService.getLatestConfiguration();
    }

    @PostMapping
    public Configuration saveConfiguration(@RequestBody Configuration configuration) {
        configuration.setCreatedAt(LocalDateTime.now());
        return configurationService.saveConfiguration(configuration);
    }
}
