package manager.input.consumers;

import lombok.extern.slf4j.Slf4j;
import manager.input.Secret;
import manager.input.dto.Address;
import manager.input.producers.ToManagerOutputs;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class FromClientAuth {
    private final ToManagerOutputs prod;

    public FromClientAuth(ToManagerOutputs toMainManagerInputs) {
        Objects.requireNonNull(toMainManagerInputs);
        this.prod = toMainManagerInputs;
    }


    @KafkaListener(
            topics = "client-auth-manager-input",
            groupId = "client-auth-manager-input",
            concurrency = "1",
            containerFactory = "getAddress"
    )
    public void consume(final ConsumerRecord<String, Address> record) {
        if (!Objects.equals(record.key(), "default")) {
            return;
        }
        log.info("Start FromClientAuth");
        log.info("Received message {}", record.value());
        try {
            prod.send(
                    record.value(),
                    "default",
                    "manager-input",
                    "main-manager-output"
            );
            prod.send(
                    new Address(Secret.decrypt(record.value().getAddress())),
                    "default",
                    "manager-input",
                    "manager-output"
            );
        }catch (final Throwable e) {
            log.info("Error: {}", e.getMessage());
        }
    }
}
