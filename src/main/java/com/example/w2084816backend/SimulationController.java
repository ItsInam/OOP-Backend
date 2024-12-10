package com.example.w2084816backend;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {
    @Autowired
    private SimulationService simulationService;

    @PostMapping("/start")
    public String startSimulation(@RequestBody Configuration configuration) throws SQLException {
        simulationService.startSimulation(configuration);
        return "Simulation started!";
    }

    @PostMapping("/stop")
    public String stopSimulation() {
        simulationService.stopSimulation();
        return "Simulation stopped!";
    }
}
