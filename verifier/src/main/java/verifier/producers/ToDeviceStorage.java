package verifier.producers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import verifier.dto.MessageAuth;

import java.util.Map;

import static verifier.conf.Kafka.ALL_ACKS_KAFKA_TEMPLATE;

@Component
@Slf4j
public class ToDeviceStorage {
    private final Map<String,
            KafkaTemplate<String, MessageAuth>> templates;
    public ToDeviceStorage(
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
    }
}
