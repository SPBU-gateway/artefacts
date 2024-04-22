package main.manager.output.producers;

import lombok.extern.slf4j.Slf4j;
import main.manager.output.dto.MessageGetDevices;
import main.manager.output.dto.NewDevice;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static main.manager.output.conf.Kafka.ALL_ACKS_KAFKA_TEMPLATE;

@Component
@Slf4j
public class ToMainStorage {
    private final Map<String,
            KafkaTemplate<String, MessageGetDevices>> templates;
    public ToMainStorage(
            final @Qualifier(ALL_ACKS_KAFKA_TEMPLATE)
            KafkaTemplate<String, MessageGetDevices> allAcksKafkaTemplate
    ) {
        templates = Map.of(
                "all", allAcksKafkaTemplate
        );
    }

    public void send(final MessageGetDevices event) {
        log.info("Start sending {}", event);
        templates.get("all")
                .send(
                        "monitor",
                        event
                );
    }
}
