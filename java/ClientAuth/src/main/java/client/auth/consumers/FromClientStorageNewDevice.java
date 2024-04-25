package client.auth.consumers;

import client.auth.CredentialsNewDevice;
import client.auth.Device;
import client.auth.Secret;
import client.auth.producers.ToManagerInputNewDevice;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromClientStorageNewDevice {
    private final ToManagerInputNewDevice prod;

    public FromClientStorageNewDevice(ToManagerInputNewDevice prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "client-storage-client-auth",
            groupId = "client-storage-client-auth-new-device",
            concurrency = "1",
            containerFactory = "credentialsNewDevice"
    )
    public void consume(final ConsumerRecord<String, CredentialsNewDevice> record) {
        if (!Objects.equals(record.key(), "new-device")) {
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
            String decryptedDeviceName = Secret.decrypt(decryptedPassword, record.value().getDevice().getName());
            log.info("Decrypted Device Name: {}", decryptedDeviceName);
            String decryptedDevicePassword = Secret.decrypt(
                    decryptedPassword, record.value().getDevice().getPassword()
            );
            log.info("Decrypted Device Password: {}", decryptedDevicePassword);
            String newEncryptedDeviceName = Secret.encrypt(decryptedDevicePassword, decryptedDeviceName);
            log.info("New Encrypted Device Name: {}", newEncryptedDeviceName);
            String newEncryptedDevicePassword = Secret.encrypt(decryptedDevicePassword);
            log.info("New Encrypted Device Password: {}", newEncryptedDevicePassword);
            prod.send(
                    new Device(
                            newEncryptedDeviceName,
                            newEncryptedDevicePassword
                    ),
                    "new-device",
                    "client-auth",
                    "manager-input"
            );
        } catch (Throwable ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromClientAuth");
    }
}
