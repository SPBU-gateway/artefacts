package verifier.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import verifier.Secret;
import verifier.dto.Device;
import verifier.dto.MessageAuth;
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
            topics = "to-verifier-authenticated-devices",
            groupId = "to-verifier-authenticated-devices",
            concurrency = "1",
            containerFactory = "messageAuthConsumer"
    )
    @Transactional
    public void consume(final ConsumerRecord<String, MessageAuth> record) throws Exception {
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
                name = Secret.decrypt(device.getName());
                message = Secret.decrypt(device.getMessage());
                devices.add(
                        new Device(
                                name,
                                password,
                                message
                        )
                );
            } catch (Throwable e) {
                log.info("Этот девай вонючий чёрт: {}", name);
            }
        }
        try {
            prod.send(new MessageAuth(
                    "Verifier",
                    "MainManagerOutput",
                    Secret.decrypt(record.value().getAddress()),
                    devices
            ));
        }catch (Throwable e) {
            log.info("Error: {}", e.getMessage());
        }
    }
}
