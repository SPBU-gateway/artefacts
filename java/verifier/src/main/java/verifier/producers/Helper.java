package verifier.producers;

import org.apache.kafka.clients.producer.ProducerRecord;
import verifier.dto.MessageAuthWithPassword;

public abstract class Helper {
    protected static <T> ProducerRecord<String, T> formMessage(final T event, final String key, final String from, final String to) {
        var rec = new ProducerRecord<String, T>(
                "monitor",
                key,
                event
        );
        rec.headers().add("from", from.getBytes());
        rec.headers().add("to", to.getBytes());
        return rec;
    }
}
