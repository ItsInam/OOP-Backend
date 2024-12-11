package com.example.w2084816backend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;

/**
 * The SimulationController class provides REST endpoints for starting and stopping the simulation.
 * It handles incoming HTTP POST requests to start and stop the simulation, delegating the logic to the SimulationService.
 */

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {
    @Autowired
    private SimulationService simulationService; // Injecting the SimulationService to manage the simulation flow

    // Starts simulation by calling simulationService
    @PostMapping("/start")
    public String startSimulation(@RequestBody Configuration configuration) throws SQLException {
        simulationService.startSimulation(configuration);
        return "Simulation started!";
    }
    // Stop the simulation by calling simulationService
    @PostMapping("/stop")
    public String stopSimulation() {
        simulationService.stopSimulation();
        return "Simulation stopped!";
    }
}
