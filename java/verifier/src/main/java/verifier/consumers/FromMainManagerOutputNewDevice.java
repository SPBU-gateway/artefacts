package verifier.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import verifier.Secret;
import verifier.dto.NewDevice;
import verifier.producers.ToDeviceStorageNewDevice;

import java.util.Objects;

@Component
@Slf4j
public class FromMainManagerOutputNewDevice {
    private final ToDeviceStorageNewDevice prod;

    public FromMainManagerOutputNewDevice(ToDeviceStorageNewDevice aProd) {
        Objects.requireNonNull(aProd);
        this.prod = aProd;
    }

    @KafkaListener(
            topics = "main-manager-output-verifier",
            groupId = "main-manager-output-verifier-new-device",
            concurrency = "1",
            containerFactory = "newDeviceConsumer"
    )
    @Transactional
    public void consume(final ConsumerRecord<String, NewDevice> record) throws Exception {
        if (!Objects.equals(record.key(), "new-device")){
            return;
        }
        log.info("Start register new device: {}", record.value());
        Objects.requireNonNull(record.value(), "Device must not be null");
        prod.send(
                new NewDevice(
                        record.value().getName(),
                        record.value().getPassword()
                ),
                "new-device",
                "verifier",
                "device-storage"
        );
        log.info("End register new device");
    }
}
