package client.storage.producers;

import client.storage.CredentialsAddress;
import client.storage.conf.Kafka;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ToClientAuth extends Helper{
    private final KafkaTemplate<String, CredentialsAddress> kafka;

    public ToClientAuth(
            @Qualifier(Kafka.CREDENTIALS_ADDRESS)
            KafkaTemplate<String, CredentialsAddress> kafka
    ) {
        this.kafka = kafka;
    }

    public void send(CredentialsAddress event, String key, String from, String to) {
        log.info("Start sending credentials: {}", event);
        kafka.send(formMessage(
            event,
            key,
            from,
            to
        ));
        log.info("End sending credentials: {}", event);
    }
}
