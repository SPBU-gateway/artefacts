package client.auth.producers;

import client.auth.conf.Kafka;
import client.auth.GetCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ToClientStorage extends Helper{
    private final KafkaTemplate<String, GetCredentials> kafkaTemplate;

    public ToClientStorage(
            @Qualifier(Kafka.GET_CREDENTIALS_PROD)
            final KafkaTemplate<String, GetCredentials> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(GetCredentials event, String key, String from, String to) {
        log.info("Start sending credentials: {}", event);
        kafkaTemplate.send(formMessage(event, key, from, to));
        log.info("End sending credentials: {}", event);
    }
}
