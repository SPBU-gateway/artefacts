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
            topics = "manager-input-main-manager-output",
            groupId = "manager-input-main-manager-output",
            concurrency = "1",
            containerFactory = "getMessageGetDevices"
    )
    public void consume(final ConsumerRecord<String, MessageGetDevices> record) throws Exception {
        if (!Objects.equals(record.key(), "default")){
            return;
        }
        log.info("Start FromManagerInput");
        log.info(
                "Received message: {}",
                record.value().toString()
        );
        prod.send(
                new MessageGetDevices(
                        record.value().getAddress()
                ),
                "default",
                "main-manager-output",
                "main-storage"
        );
        log.info("End FromManagerInput");
    }
}
