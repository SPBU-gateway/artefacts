package verifier.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import verifier.dto.MessageAuth;
import verifier.dto.MessageAuthWithPassword;
import verifier.producers.ToDeviceStorage;

import java.util.Objects;

@Component
@Slf4j
public class FromMainManagerOutput {
    private final ToDeviceStorage prod;

    public FromMainManagerOutput(ToDeviceStorage prod) {
        Objects.requireNonNull(prod);
        this.prod = prod;
    }

    @KafkaListener(
            topics = "main-manager-output-verifier",
            groupId = "main-manager-output-verifier",
            concurrency = "1",
            containerFactory = "messageAuthConsumer"
    )
    @Transactional
    public void consume(final ConsumerRecord<String, MessageAuth> record) throws Exception {
        if (!Objects.equals(record.key(), "default")){
            return;
        }
        log.info(
                "Received message: {}",
                record.value().toString()
        );
        prod.send(record.value(), "default", "verifier", "device-storage");
    }
}
