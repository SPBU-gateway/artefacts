package manager.input.producers;

import manager.input.dto.GetDevices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Objects;

import static manager.input.conf.Kafka.GET_DEVICES_PROD;

@Configuration
@Slf4j
public class ToClientAuth extends Helper{
    private final KafkaTemplate<String, GetDevices> kafka;

    public ToClientAuth(
            @Qualifier(GET_DEVICES_PROD)
            final KafkaTemplate<String, GetDevices> kafka) {
        Objects.requireNonNull(kafka);
        this.kafka = kafka;
    }
    public void send(GetDevices getDevices, String key, String from, String to){
        log.info("Start sending {}", getDevices);
        kafka.send(formMessage(getDevices, key, from, to));
        log.info("End sending {}", getDevices);
    }
}
