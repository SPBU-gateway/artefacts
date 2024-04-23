package device.storage.producers;

import device.storage.dto.MessageAuth;
import device.storage.dto.MessageAuthWithPassword;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static device.storage.conf.Kafka.ALL_ACKS_KAFKA_TEMPLATE;

@Component
@Slf4j
public class ManagerOut {
    private final Map<String,
            KafkaTemplate<String, MessageAuthWithPassword>> templates;

    public ManagerOut(
            final @Qualifier(ALL_ACKS_KAFKA_TEMPLATE)
            KafkaTemplate<String, MessageAuthWithPassword> allAcksKafkaTemplate
    ) {
        templates = Map.of(
                "all", allAcksKafkaTemplate
        );
    }

    @Transactional
    public void send(MessageAuthWithPassword event, final String key, final String from, final String to) {
        log.info("Start sending {}", event);

        var rec = new ProducerRecord<>(
                "monitor",
                key,
                event
        );
        rec.headers().add("from", from.getBytes());
        rec.headers().add("to", to.getBytes());

        templates.get("all").send(rec);
        log.info("End sending {}", event);
    }
}
