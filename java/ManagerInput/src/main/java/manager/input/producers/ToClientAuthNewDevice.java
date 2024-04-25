package manager.input.producers;

import manager.input.dto.GetCredentialsNewDevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static manager.input.conf.Kafka.GET_CREDENTIALS_NEW_DEVICE_PROD;

@Component
@Slf4j
public class ToClientAuthNewDevice extends Helper{
    private final KafkaTemplate<String, GetCredentialsNewDevice> kafka;

    public ToClientAuthNewDevice(
            @Qualifier(GET_CREDENTIALS_NEW_DEVICE_PROD)
            final KafkaTemplate<String, GetCredentialsNewDevice> kafkaTemplate) {
        Objects.requireNonNull(kafkaTemplate);
        this.kafka = kafkaTemplate;
    }

    public void send(GetCredentialsNewDevice event, String key, String from, String to) {
        log.info("Start sending new device: {}", event);
        kafka.send(formMessage(event, key, from, to));
        log.info("Sent message: {} to topic: {}", event, to);
    }
}
