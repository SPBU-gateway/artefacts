package verifier.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import verifier.Secret;
import verifier.dto.Device;
import verifier.dto.DeviceWithPassword;
import verifier.dto.MessageAuth;
import verifier.dto.MessageAuthWithPassword;
import verifier.producers.ToManagerOutput;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class FromDeviceStorage {
    private final ToManagerOutput prod;

    public FromDeviceStorage(ToManagerOutput prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "device-storage-verifier",
            groupId = "device-storage-verifier",
            concurrency = "1",
            containerFactory = "messageAuthConsumerWithPassword"
    )
    @Transactional
    public void consume(final ConsumerRecord<String, MessageAuthWithPassword> record) throws Exception {
        if (!Objects.equals(record.key(), "default")) {
            return;
        }
        log.info(
                "Received message: {}",
                record.value().toString()
        );
        List<Device> devices = new LinkedList<>();
        String name = null;
        String password = null;
        String message = null;
        for (var device : record.value().getDevices()) {
            try {
                password = Secret.decrypt(device.getPassword());
                name = Secret.decrypt(password, device.getName());
                message = Secret.decrypt(password, device.getMessage());
                devices.add(
                        new Device(
                                name,
                                message
                        )
                );
            } catch (Throwable e) {
                log.info("Этот девай вонючий чёрт: {}", device.getName());
            }
        }
        try {
            prod.send(new MessageAuth(
                            Secret.decrypt(record.value().getAddress()),
                            devices
                    ),
                        "default",
                    "verifier",
                    "main-manager-output"
            );
        } catch (Throwable e) {
            log.info("Error: {}", e.getMessage());
        }
    }
}
