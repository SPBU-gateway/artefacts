package verifier.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import verifier.Secret;
import verifier.dto.Device;
import verifier.dto.MessageToAuth;
import verifier.producers.ManagerOut;
import verifier.repo.DeviceRepo;

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
            topics = "to-verifier-auth",
            groupId = "to-verifier-auth",
            concurrency = "1",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void consume(final ConsumerRecord<String, MessageToAuth> record) throws Exception {
        log.info(
                "Received message: {}",
                record.value().toString()
        );
        List<Device> devices = new LinkedList<>();
        log.info("Start analyzing devices");
        for (var device : record.value().getDevices()) {
            log.info("Analyzing device: {}", device.getName());
            var password = new String("some string");
            var password2 = new String("some string2");
            try {
                password = Secret.decrypt(repo.getPasswordByName(device.getName()));
                password2 = Secret.decrypt(device.getPassword());
            } catch (Exception e) {
                log.info("ERROR: {}", e.getMessage());
            }
            if (password2.equals(password)) {
                log.info("Device: {} is authorized", device.getName());
                devices.add(device);
            }
        }
        log.info("End analyzing devices");
        prod.send(new MessageToAuth(
                "Verifier",
                "ManagerOutput",
                devices
        ));
    }
}
