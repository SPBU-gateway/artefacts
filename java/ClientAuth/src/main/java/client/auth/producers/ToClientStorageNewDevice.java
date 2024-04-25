package client.auth.producers;

import client.auth.GetCredentialsNewDevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static client.auth.conf.Kafka.GET_CREDENTIALS_NEW_DEVICE_PROD;

@Component
@Slf4j
public class ToClientStorageNewDevice extends Helper {
    private final KafkaTemplate<String, GetCredentialsNewDevice> kafkaTemplate;

    public ToClientStorageNewDevice(
            @Qualifier(GET_CREDENTIALS_NEW_DEVICE_PROD) final KafkaTemplate<String, GetCredentialsNewDevice> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(GetCredentialsNewDevice event, String key, String from, String to) {
        log.info("Start sending new device: {}", event);
        kafkaTemplate.send(formMessage(event, key, from, to));
        log.info("End sending new device: {}", event);
    }
}
