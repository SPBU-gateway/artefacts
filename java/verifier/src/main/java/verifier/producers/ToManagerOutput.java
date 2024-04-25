package verifier.producers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import verifier.dto.MessageAuth;

import java.util.Map;

import static verifier.conf.Kafka.MESSAGE_AUTH;

@Component
@Slf4j
public class ToManagerOutput extends Helper {
    private final Map<String,
            KafkaTemplate<String, MessageAuth>> templates;

    public ToManagerOutput(
            final @Qualifier(MESSAGE_AUTH)
            KafkaTemplate<String, MessageAuth> allAcksKafkaTemplate
    ) {
        templates = Map.of(
                "all", allAcksKafkaTemplate
        );
    }

    public void send(final MessageAuth event, final String key, final String from, final String to) {
        log.info("Start sending {}", event);
        templates.get("all").send(formMessage(
                event, key, from, to
        ));
        log.info("End sending {}", event);
    }
}
