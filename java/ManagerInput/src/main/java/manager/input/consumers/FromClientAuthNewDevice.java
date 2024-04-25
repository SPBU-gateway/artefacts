package manager.input.consumers;

import lombok.extern.slf4j.Slf4j;
import manager.input.dto.Device;
import manager.input.producers.ToMainManagerOutputNewDevice;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromClientAuthNewDevice {
    private final ToMainManagerOutputNewDevice prod;

    public FromClientAuthNewDevice(ToMainManagerOutputNewDevice toMainManagerInputs) {
        Objects.requireNonNull(toMainManagerInputs);
        this.prod = toMainManagerInputs;
    }


    @KafkaListener(
            topics = "client-auth-manager-input",
            groupId = "client-auth-manager-input-new-device",
            concurrency = "1",
            containerFactory = "getDevice"
    )
    public void consume(final ConsumerRecord<String, Device> record) {
        if (!Objects.equals(record.key(), "new-device")) {
            return;
        }
        log.info("Start FromClientAuth");
        log.info("Received message {}", record.value());
        try {
            prod.send(
                    record.value(),
                    "new-device",
                    "manager-input",
                    "main-manager-output"
            );
        }catch (final Throwable e) {
            log.info("Error: {}", e.getMessage());
        }
    }
}
