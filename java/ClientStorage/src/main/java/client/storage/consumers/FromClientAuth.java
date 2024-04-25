package client.storage.consumers;

import client.storage.GetCredentials;
import client.storage.ClientRepo;
import client.storage.Credentials;
import client.storage.CredentialsAddress;
import client.storage.producers.ToClientAuth;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromClientAuth {
    private final ClientRepo clientRepo;
    private final ToClientAuth prod;

    public FromClientAuth(ClientRepo clientRepo, ToClientAuth prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
        Objects.requireNonNull(clientRepo, "clientRepo is null");
        this.clientRepo = clientRepo;
    }

    @KafkaListener(
            topics = "client-auth-client-storage",
            groupId = "client-auth-client-storage",
            concurrency = "1",
            containerFactory = "getCredentials"
    )
    public void consume(final ConsumerRecord<String, GetCredentials> record) {
        if (!Objects.equals(record.key(), "default")) {
            return;
        }
        log.info("Starting FromClientAuth");
        try {
            log.info(
                    "Received message: {}",
                    record.value()
            );

            Credentials credentials = clientRepo.getCredentialsByName(record.value().name);
            prod.send(
                    new CredentialsAddress(
                            record.value().getAddress(),
                            credentials
                    ),
                    "default",
                    "client-storage",
                    "client-auth"
            );
        } catch (Throwable ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromClientAuth");
    }
}
