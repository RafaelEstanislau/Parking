package com.estapar.parking.infrastructure.client;

import com.estapar.parking.application.dto.GarageConfigurationDto;
import com.estapar.parking.application.port.GarageSimulatorPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GarageSimulatorClient implements GarageSimulatorPort {

    private final RestTemplate restTemplate;
    private final String simulatorBaseUrl;

    public GarageSimulatorClient(RestTemplate restTemplate,
                                 @Value("${simulator.base-url}") String simulatorBaseUrl) {
        this.restTemplate = restTemplate;
        this.simulatorBaseUrl = simulatorBaseUrl;
    }

    @Override
    public GarageConfigurationDto fetchGarageConfiguration() {
        return restTemplate.getForObject(simulatorBaseUrl + "/garage", GarageConfigurationDto.class);
    }
}
