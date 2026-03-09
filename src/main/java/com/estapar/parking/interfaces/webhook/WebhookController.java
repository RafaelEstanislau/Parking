package com.estapar.parking.interfaces.webhook;

import com.estapar.parking.application.dto.WebhookEventDto;
import com.estapar.parking.application.usecase.ProcessEntryEventUseCase;
import com.estapar.parking.application.usecase.ProcessExitEventUseCase;
import com.estapar.parking.application.usecase.ProcessParkedEventUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final ProcessEntryEventUseCase processEntryEventUseCase;
    private final ProcessParkedEventUseCase processParkedEventUseCase;
    private final ProcessExitEventUseCase processExitEventUseCase;

    public WebhookController(ProcessEntryEventUseCase processEntryEventUseCase,
                             ProcessParkedEventUseCase processParkedEventUseCase,
                             ProcessExitEventUseCase processExitEventUseCase) {
        this.processEntryEventUseCase = processEntryEventUseCase;
        this.processParkedEventUseCase = processParkedEventUseCase;
        this.processExitEventUseCase = processExitEventUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> handleEvent(@Valid @RequestBody WebhookEventDto event) {
        switch (event.eventType()) {
            case "ENTRY" -> processEntryEventUseCase.execute(event);
            case "PARKED" -> processParkedEventUseCase.execute(event);
            case "EXIT" -> processExitEventUseCase.execute(event);
            default -> throw new IllegalArgumentException("Unknown event type: " + event.eventType());
        }
        return ResponseEntity.ok().build();
    }
}
