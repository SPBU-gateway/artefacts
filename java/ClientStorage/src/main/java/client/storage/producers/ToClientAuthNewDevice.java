package client.storage.producers;

import client.storage.CredentialsNewDevice;
import client.storage.conf.Kafka;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class ToClientAuthNewDevice extends Helper{
    private final KafkaTemplate<String, CredentialsNewDevice> kafka;

    public ToClientAuthNewDevice(
            @Qualifier(Kafka.CREDENTIALS_NEW_DEVICE)
            KafkaTemplate<String, CredentialsNewDevice> kafka
    ) {
        this.kafka = kafka;
    }

    public void send(CredentialsNewDevice event, String key, String from, String to) {
        log.info("Start sending new device: {}", event);
        kafka.send(formMessage(
                event,
                key,
                from,
                to
        ));
        log.info("End sending new device: {}", event);
    }
}
