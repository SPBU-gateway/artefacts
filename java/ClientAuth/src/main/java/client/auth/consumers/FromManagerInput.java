package client.auth.consumers;

import client.auth.GetCredentials;
import client.auth.producers.ToClientStorage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromManagerInput {
    private final ToClientStorage prod;

    public FromManagerInput(ToClientStorage prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "manager-input-client-auth",
            groupId = "manager-input-client-auth",
            concurrency = "1",
            containerFactory = "getCredentials"
    )
    public void consume(final ConsumerRecord<String, GetCredentials> record) {
        if (!Objects.equals(record.key(), "default")) {
            return;
        }
        log.info("Starting FromClientAuth");
        log.info(
                "Received message: {}",
                record.value()
        );
        try {
            prod.send(
                    new GetCredentials(
                            record.value().getName(),
                            record.value().getAddress()
                    ),
                    "default",
                    "client-auth",
                    "client-storage"
            );
        } catch (Exception ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromClientAuth");
    }
}
