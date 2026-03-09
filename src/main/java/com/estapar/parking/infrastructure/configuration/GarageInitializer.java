package com.estapar.parking.infrastructure.configuration;

import com.estapar.parking.application.usecase.InitializeGarageUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class GarageInitializer implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(GarageInitializer.class);

    private final InitializeGarageUseCase initializeGarageUseCase;

    public GarageInitializer(InitializeGarageUseCase initializeGarageUseCase) {
        this.initializeGarageUseCase = initializeGarageUseCase;
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Initializing garage configuration from simulator...");
        initializeGarageUseCase.execute();
        logger.info("Garage configuration initialized successfully.");
    }
}
