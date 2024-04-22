package main.manager.output.producers;

import lombok.extern.slf4j.Slf4j;
import main.manager.output.dto.MessageAuth;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

import static main.manager.output.conf.Kafka.ALL_ACKS_KAFKA_TEMPLATE;

@Component
@Slf4j
public class ToVerifier {
    private final Map<String,
            KafkaTemplate<String, MessageAuth>> templates;
    public ToVerifier(
            final @Qualifier(ALL_ACKS_KAFKA_TEMPLATE)
            KafkaTemplate<String, MessageAuth> allAcksKafkaTemplate
    ) {
        templates = Map.of(
                "all", allAcksKafkaTemplate
        );
    }

    public void send(final MessageAuth event) {
        log.info("Start sending {}", event);
        templates.get("all")
                .send(
                        "monitor",
                        event
                );
        log.info("End sending {}", event);
    }
}
