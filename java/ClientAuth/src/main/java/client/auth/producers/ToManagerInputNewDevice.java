package client.auth.producers;

import client.auth.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static client.auth.conf.Kafka.DEVICE_PROD;

@Component
@Slf4j
public class ToManagerInputNewDevice extends Helper{
    private final KafkaTemplate<String, Device> kafka;

    public ToManagerInputNewDevice(
            @Qualifier(DEVICE_PROD)
            final KafkaTemplate<String, Device> kafkaTemplate) {
        this.kafka = kafkaTemplate;
    }

    public void send(final Device event, final String key, final String from, final String to) {
        log.info("Start sending {}", event);
        kafka.send(formMessage(event, key, from, to));
        log.info("End sending {}", event);
    }
}
