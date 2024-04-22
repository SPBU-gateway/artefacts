package verifier.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import verifier.Secret;
import verifier.dto.NewDevice;
import verifier.repo.DeviceRepo;
import java.util.Objects;

@Component
@Slf4j
public class ManagerInNewDevice {
    private final DeviceRepo repo;

    public ManagerInNewDevice(DeviceRepo repo) {
        Objects.requireNonNull(repo);
        this.repo = repo;
    }

    @KafkaListener(
            topics = "to-verifier-new-device",
            groupId = "to-verifier-new-device",
            concurrency = "1",
            containerFactory = "newDeviceConsumer"
    )
    @Transactional
    public void consume(final ConsumerRecord<String, NewDevice> record) throws Exception {
        log.info("Start register new device: {}", record.value());
        Objects.requireNonNull(record.value(), "Device must not be null");
        try {
            repo.addDevice(
                    record.value().getName(),
                    Secret.encrypt(record.value().getPassword())
            );
        }catch(RuntimeException ex) {
            log.info("ERROR: {}", ex.getMessage());
        }

        log.info("End register new device");
    }
}
