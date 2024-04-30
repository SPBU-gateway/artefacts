package main.manager.output.consumers;

import lombok.extern.slf4j.Slf4j;
import main.manager.output.dto.NewDevice;
import main.manager.output.producers.VerifierOutNewDevice;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Component
@Slf4j
public class ManagerInNewDevice {
    private final VerifierOutNewDevice out;

    public ManagerInNewDevice(VerifierOutNewDevice out) {
        Objects.requireNonNull(out, "out is null");
        this.out = out;
    }


    @KafkaListener(
            topics = "manager-input-main-manager-output",
            groupId = "manager-input-main-manager-output-new-device",
            concurrency = "1",
            containerFactory = "getNewDevice"
    )
    @Transactional
    public void consume(final ConsumerRecord<String, NewDevice> record) throws Exception {
        if (!Objects.equals(record.key(), "new-device")){
            return;
        }
        log.info("Start register new device: {}", record.value());
        Objects.requireNonNull(record.value(), "Device must not be null");
        out.send(record.value(), "new-device", "main-manager-output", "verifier");
        log.info("End register new device");
    }
}
