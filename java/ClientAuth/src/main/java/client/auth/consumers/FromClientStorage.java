package client.auth.consumers;

import client.auth.CredentialsAddress;
import client.auth.Secret;
import client.auth.MessageGetDevices;
import client.auth.producers.ToManagerInput;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromClientStorage {
    private final ToManagerInput prod;

    public FromClientStorage(ToManagerInput prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "client-storage-client-auth",
            groupId = "client-storage-client-auth",
            concurrency = "1",
            containerFactory = "getCredentialsAddress"
    )
    public void consume(final ConsumerRecord<String, CredentialsAddress> record) {
        if (!Objects.equals(record.key(), "default")) {
            return;
        }
        log.info("Starting FromClientAuth");
        try {
            log.info(
                    "Received message: {}",
                    record.value()
            );
            String decryptedPassword = Secret.decrypt(record.value().getCredentials().getPassword());
            log.info("Decrypted Password: {}", decryptedPassword);
            String decryptedAddress = Secret.decrypt(decryptedPassword, record.value().getAddress());
            log.info("Decrypted Address: {}", decryptedAddress);
            String newEncryptedAddress = Secret.encrypt(decryptedAddress);
            log.info("New Encrypted Address: {}", newEncryptedAddress);
            prod.send(
                    new MessageGetDevices(
                            newEncryptedAddress
                    ),
                    "default",
                    "client-auth",
                    "manager-input"
            );
        } catch (Throwable ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromClientAuth");
    }
}
