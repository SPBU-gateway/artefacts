package verifier.producers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import verifier.dto.NewDevice;

import java.util.Map;

import static verifier.conf.Kafka.NEW_DEVICE;

@Component
@Slf4j
public class ToDeviceStorageNewDevice extends Helper{
    private final Map<String,
            KafkaTemplate<String, NewDevice>> templates;
    public ToDeviceStorageNewDevice(
            final @Qualifier(NEW_DEVICE)
            KafkaTemplate<String, NewDevice> allAcksKafkaTemplate
    ) {
        templates = Map.of(
                "all", allAcksKafkaTemplate
        );
    }
    public void send(final NewDevice event, final String key, final String from, final String to) {
        log.info("Start sending {}", event);
        templates.get("all")
                .send(formMessage(event, key, from, to));
    }
}
