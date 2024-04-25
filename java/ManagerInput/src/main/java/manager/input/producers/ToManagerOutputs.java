package manager.input.producers;

import lombok.extern.slf4j.Slf4j;
import manager.input.dto.Address;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static manager.input.conf.Kafka.ADDRESS_PROD;

@Component
@Slf4j
public class ToManagerOutputs extends Helper{
    private final KafkaTemplate<String, Address> kafka;

    public ToManagerOutputs(
            @Qualifier(ADDRESS_PROD)
            final KafkaTemplate<String, Address> kafkaTemplate) {
        Objects.requireNonNull(kafkaTemplate);
        this.kafka = kafkaTemplate;
    }

    public void send(Address event, String key, String from, String to) {
        log.info("Start sending new device: {}", event);
        kafka.send(formMessage(event, key, from, to));
        log.info("End sending message: {}", event);
    }
}
