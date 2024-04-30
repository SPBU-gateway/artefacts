package device.storage.consumers;

import device.storage.dto.Device;
import device.storage.dto.DeviceWithPassword;
import device.storage.dto.MessageAuth;
import device.storage.dto.MessageAuthWithPassword;
import device.storage.producers.ManagerOut;
import device.storage.repo.DeviceRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class ManagerInAuth {
    private final DeviceRepo repo;
    private final ManagerOut prod;

    public ManagerInAuth(DeviceRepo repo, ManagerOut prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
        Objects.requireNonNull(repo);
        this.repo = repo;
    }

    @KafkaListener(
            topics = "verifier-device-storage",
            groupId = "verifier-device-storage",
            concurrency = "1",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(final ConsumerRecord<String, MessageAuth> record) throws Exception {
        if (!Objects.equals(record.key(), "default")) {
            return;
        }
        log.info(
                "Received message: {}",
                record.value().toString()
        );
        List<DeviceWithPassword> devices = new LinkedList<>();
        log.info("Start get devices");
        for (var device : record.value().getDevices()) {
            try {
                log.info("Start analyzing device: {}", device.getName());
                var d = repo.getDevicesByName(device.getName());
                log.info("Get from repo: {}", d);
                Objects.requireNonNull(d, "Device not found");
                devices.add(
                        new DeviceWithPassword(
                                d.getName(),
                                d.getPassword(),
                                device.getMessage()
                        )
                );
            } catch (Throwable ex) {
                log.info("ERROR: {}", ex.getMessage());
            }
        }
        log.info("End analyzing devices");
        prod.send(new MessageAuthWithPassword(
                        record.value().getAddress(),
                        devices
                ),
                record.key(),
                "device-storage",
                "verifier"
        );
    }
}
