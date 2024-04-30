package manager.input.conf;

import manager.input.dto.Address;
import manager.input.dto.Device;
import manager.input.dto.GetCredentialsNewDevice;
import manager.input.dto.GetDevices;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Configuration
public class Kafka {
    public static final String GET_DEVICES_PROD = "getDevicesProd";
    public static final String GET_CREDENTIALS_NEW_DEVICE_PROD = "getCredentialsNewDeviceProd";
    public static final String ADDRESS_PROD = "addressProd";
    public static final String DEVICE_PROD = "deviceProd";
    private final KafkaProperties properties;

    public Kafka(KafkaProperties properties) {
        Objects.requireNonNull(properties, "KafkaProperties must not be null");
        this.properties = properties;
    }

    @Bean(GET_CREDENTIALS_NEW_DEVICE_PROD)
    public KafkaTemplate<String, GetCredentialsNewDevice> getCredentialsNewDeviceProd() {
        return new KafkaTemplate<>(producerFactory(
                props -> {
                    props.put(ProducerConfig.ACKS_CONFIG, "all");
                }
        )
        );
    }

    @Bean(GET_DEVICES_PROD)
    public KafkaTemplate<String, GetDevices> getDevicesProd() {
        return new KafkaTemplate<>(producerFactory(
                props -> {
                    props.put(ProducerConfig.ACKS_CONFIG, "all");
                }
        )
        );
    }
    @Bean(ADDRESS_PROD)
    public KafkaTemplate<String, Address> addressProd() {
        return new KafkaTemplate<>(producerFactory(
                props -> {
                    props.put(ProducerConfig.ACKS_CONFIG, "all");
                }
        )
        );
    }
    @Bean(DEVICE_PROD)
    public KafkaTemplate<String, Device> deviceProd() {
        return new KafkaTemplate<>(producerFactory(
                props -> {
                    props.put(ProducerConfig.ACKS_CONFIG, "all");
                }
        )
        );
    }

    private <T> ProducerFactory<String, T> producerFactory(
            final Consumer<Map<String, Object>> enchanter
    ) {
        var props = properties.buildProducerProperties(null);
        // Работаем со строками
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        // Партиция одна, так что все равно как роутить
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG,
                RoundRobinPartitioner.class);
        // Отправляем сообщения сразу
        props.put(ProducerConfig.LINGER_MS_CONFIG, 0);
        // До-обогащаем конфигурацию
        enchanter.accept(props);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Address>
    getAddress() {
        var props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        ConcurrentKafkaListenerContainerFactory<String, Address>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new JsonDeserializer<>(Address.class)
                )
        );
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Device>
    getDevice() {
        var props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        ConcurrentKafkaListenerContainerFactory<String, Device>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new JsonDeserializer<>(Device.class)
                )
        );
        return factory;
    }
}
