package main.manager.output.consumers;

import lombok.extern.slf4j.Slf4j;
import main.manager.output.dto.MessageAuth;
import main.manager.output.producers.ToVerifier;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromMainStorage {
    private final ToVerifier prod;

    public FromMainStorage(ToVerifier prod) {
        Objects.requireNonNull(prod, "prod is null");
        this.prod = prod;
    }

    @KafkaListener(
            topics = "main-storage-main-manager-output",
            groupId = "main-storage-main-manager-output",
            concurrency = "1",
            containerFactory = "getMessageAuth"
    )
    public void consume(final ConsumerRecord<String, MessageAuth> record) {
        if (!Objects.equals(record.key(), "default")){
            return;
        }
        log.info(
                "Received message: {}",
                record.value().toString()
        );
        prod.send(
                record.value(),
                "default",
                "main-manager-output",
                "verifier"
        );
        log.info("End FromMainStorage");
    }
}
