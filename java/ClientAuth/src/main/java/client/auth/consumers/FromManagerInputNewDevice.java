package client.auth.consumers;

import client.auth.producers.ToClientStorageNewDevice;
import client.auth.GetCredentialsNewDevice;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromManagerInputNewDevice {
    private final ToClientStorageNewDevice prod;

    public FromManagerInputNewDevice(ToClientStorageNewDevice prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "manager-input-client-auth",
            groupId = "manager-input-client-auth-new-device",
            concurrency = "1",
            containerFactory = "getCredentialsNewDevice"
    )
    public void consume(final ConsumerRecord<String, GetCredentialsNewDevice> record) {
        if (!Objects.equals(record.key(), "new-device")) {
            return;
        }
        log.info("Starting FromManagerInputNewDevice");
        try {
            log.info(
                    "Received message: {}",
                    record.value()
            );
            prod.send(
                    record.value(),
                    "new-device",
                    "client-auth",
                    "client-storage"
            );
        } catch (Throwable ex) {
            log.info("Error: {}", ex.getMessage());
        }
        log.info("End FromManagerInputNewDevice");
    }
}
