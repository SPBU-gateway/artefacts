package manager.input.producers;

import org.apache.kafka.clients.producer.ProducerRecord;

public abstract class Helper {
    protected static <T> ProducerRecord<String, T> formMessage(final T event, final String key, final String from, final String to) {
        var rec = new ProducerRecord<>(
                "monitor",
                key,
                event
        );
        rec.headers().add("from", from.getBytes());
        rec.headers().add("to", to.getBytes());
        return rec;
    }
}
