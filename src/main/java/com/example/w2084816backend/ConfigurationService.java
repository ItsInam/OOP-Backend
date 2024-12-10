package com.example.w2084816backend;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {
    @Autowired
    private ConfigurationRepository configurationRepository;

    public Configuration getLatestConfiguration() {
        return configurationRepository.findFirstByOrderByIdDesc();
    }

    public Configuration saveConfiguration(Configuration configuration) {
        return configurationRepository.save(configuration);
    }
}
