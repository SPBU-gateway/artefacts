package main.manager.output.producers;

import lombok.extern.slf4j.Slf4j;
import main.manager.output.dto.NewDevice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static main.manager.output.conf.Kafka.NEW_DEVICE;

@Component
@Slf4j
public class VerifierOutNewDevice extends Helper{
    private final Map<String,
            KafkaTemplate<String, NewDevice>> templates;
    public VerifierOutNewDevice(
            final @Qualifier(NEW_DEVICE)
            KafkaTemplate<String, NewDevice> newDevice
    ) {
        templates = Map.of(
                "all", newDevice
        );
    }

    public void send(final NewDevice event, final String key, final String from, final String to) {
        log.info("Start sending {}", event);
        templates.get("all")
                .send(formMessage(event, key, from, to));
        log.info("End sending {}", event);
    }
}
