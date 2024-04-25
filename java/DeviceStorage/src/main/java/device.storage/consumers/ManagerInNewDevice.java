package device.storage.consumers;

import device.storage.dto.NewDevice;
import device.storage.repo.DeviceRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Objects;

@Component
@Slf4j
public class ManagerInNewDevice {
    private final DeviceRepo repo;

    public ManagerInNewDevice(DeviceRepo repo) {
        log.info("Start ManagerInNewDevice");
        Objects.requireNonNull(repo);
        this.repo = repo;
    }

    @KafkaListener(
            topics = "verifier-device-storage",
            groupId = "verifier-device-storage-new-device",
            concurrency = "1",
            containerFactory = "newDeviceConsumer"
    )
    @Transactional
    public void consume(final ConsumerRecord<String, NewDevice> record) throws Exception {
        if (!Objects.equals(record.key(), "new-device")) {
            return;
        }
        log.info("Start register new device: {}", record.value());
        Objects.requireNonNull(record.value(), "Device must not be null");
        try {
            repo.addDevice(
                    record.value().getName(),
                    record.value().getPassword()
            );
        } catch (Throwable ex) {
            log.info("ERROR: {}", ex.getMessage());
        }

        log.info("End register new device");
    }
}
