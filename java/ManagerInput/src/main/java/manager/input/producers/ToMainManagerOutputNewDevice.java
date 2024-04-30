package manager.input.producers;

import lombok.extern.slf4j.Slf4j;
import manager.input.dto.Device;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static manager.input.conf.Kafka.DEVICE_PROD;

@Component
@Slf4j
public class ToMainManagerOutputNewDevice extends Helper{
    private final KafkaTemplate<String, Device> kafka;

    public ToMainManagerOutputNewDevice(
            @Qualifier(DEVICE_PROD)
            final KafkaTemplate<String, Device> kafka) {
        this.kafka = kafka;
    }

    public void send(Device event, String key, String from, String to) {
        log.info("Start sending {}", event);
        kafka.send(formMessage(event, key, from, to));
        log.info("Finish sending {}", event);
    }
}
