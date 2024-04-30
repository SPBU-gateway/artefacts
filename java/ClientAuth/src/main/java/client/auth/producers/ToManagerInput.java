package client.auth.producers;

import client.auth.MessageGetDevices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static client.auth.conf.Kafka.MESSAGE_GET_DEVICES_PROD;

@Component
@Slf4j
public class ToManagerInput extends Helper{
    private final KafkaTemplate<String, MessageGetDevices> kafkaTemplate;

    public ToManagerInput(
            @Qualifier(MESSAGE_GET_DEVICES_PROD)
            final KafkaTemplate<String, MessageGetDevices> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(final MessageGetDevices event, final String key, final String from, final String to) {
        log.info("Start sending {}", event);
        kafkaTemplate.send(formMessage(event, key, from, to));
        log.info("End sending {}", event);
    }
}
