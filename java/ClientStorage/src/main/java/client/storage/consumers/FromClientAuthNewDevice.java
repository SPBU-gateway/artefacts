package client.storage.consumers;

import client.storage.ClientRepo;
import client.storage.Credentials;
import client.storage.CredentialsNewDevice;
import client.storage.GetCredentialsNewDevice;
import client.storage.producers.ToClientAuthNewDevice;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromClientAuthNewDevice {
    private final ClientRepo clientRepo;
    private final ToClientAuthNewDevice prod;

    public FromClientAuthNewDevice(ClientRepo clientRepo, ToClientAuthNewDevice prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
        Objects.requireNonNull(clientRepo, "clientRepo is null");
        this.clientRepo = clientRepo;
    }

    @KafkaListener(
            topics = "client-auth-client-storage",
            groupId = "client-auth-client-storage-new-device",
            concurrency = "1",
            containerFactory = "getCredentialsNewDevice"
    )
    public void consume(final ConsumerRecord<String, GetCredentialsNewDevice> record) {
        if (!Objects.equals(record.key(), "new-device")) {
            return;
        }

        try {

            Credentials credentials = clientRepo.getCredentialsByName(record.value().name);

            log.info(
                    "Received message: {}",
                    record.value().toString()
            );
            prod.send(
                    new CredentialsNewDevice(
                            record.value().getDevice(),
                            credentials
                    ),
                    "new-device",
                    "client-storage",
                    "client-auth"
            );
        } catch (Throwable ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromMainStorage");
    }
}
