package main.manager.output.consumers;

import lombok.extern.slf4j.Slf4j;
import main.manager.output.dto.MessageGetDevices;
import main.manager.output.producers.ToMainStorage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Objects;

@Component
@Slf4j
public class FromManagerInput {
    private final ToMainStorage prod;

    public FromManagerInput(ToMainStorage prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "to-main-manager-output-get-devices",
            groupId = "to-main-manager-output-get-devices",
            concurrency = "1",
            containerFactory = "getMessageGetDevices"
    )
    public void consume(final ConsumerRecord<String, MessageGetDevices> record) throws Exception {
        log.info("Start FromManagerInput");
        log.info(
                "Received message: {}",
                record.value().toString()
        );
        prod.send(record.value());
        log.info("End FromManagerInput");
    }
}
