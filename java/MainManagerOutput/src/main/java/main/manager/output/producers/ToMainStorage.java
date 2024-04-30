package main.manager.output.producers;

import lombok.extern.slf4j.Slf4j;
import main.manager.output.dto.MessageGetDevices;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static main.manager.output.conf.Kafka.MESSAGE_GET_DEVICES;

@Component
@Slf4j
public class ToMainStorage extends Helper{
    private final KafkaTemplate<String, MessageGetDevices> messageGetDevices;

    public ToMainStorage(final @Qualifier(MESSAGE_GET_DEVICES)
                         KafkaTemplate<String, MessageGetDevices> template) {
        this.messageGetDevices = template;
    }

    public void send(final MessageGetDevices event, final String key, final String from, final String to) {
        log.info("Start sending {}", event);
        log.info("Producer type: {}", messageGetDevices.getClass());
        messageGetDevices.send(formMessage(event, key, from, to));
        log.info("End sending {}", event);
    }
}
