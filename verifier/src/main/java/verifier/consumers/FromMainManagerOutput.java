package verifier.consumers;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import verifier.Secret;
import verifier.dto.MessageAuth;
import verifier.producers.ToDeviceStorage;
import verifier.producers.ToManagerOutput;

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
            topics = "to-verifier",
            groupId = "to-verifier",
            concurrency = "1",
            containerFactory = "messageAuthConsumer"
    )
    @Transactional
    public void consume(final ConsumerRecord<String, MessageAuth> record) throws Exception {
        log.info(
                "Received message: {}",
                record.value().toString()
        );
        prod.send(new MessageAuth(
                "Verifier",
                "DeviceStorage",
                record.value().getAddress(),
                record.value().getDevices()
        ));
    }
}
