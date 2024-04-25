package client.auth.conf;

import client.auth.CredentialsAddress;
import client.auth.CredentialsNewDevice;
import client.auth.Device;
import client.auth.MessageGetDevices;
import client.auth.GetCredentials;
import client.auth.GetCredentialsNewDevice;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

@Configuration
public class Kafka {
    public static final String GET_CREDENTIALS_NEW_DEVICE_PROD = "getCredentialsNewDeviceProd";
    public static final String GET_CREDENTIALS_PROD = "getCredentialsProd";
    public static final String MESSAGE_GET_DEVICES_PROD = "messageGetDevicesProd";
    public static final String DEVICE_PROD = "deviceProd";
    private final KafkaProperties properties;

    public Kafka(KafkaProperties properties) {
        Objects.requireNonNull(properties, "KafkaProperties must not be null");
        this.properties = properties;
    }

    @Bean(GET_CREDENTIALS_PROD)
    public KafkaTemplate<String, GetCredentials> getCredentialsProd() {
        return new KafkaTemplate<>(producerFactory(
                props -> {
                    props.put(ProducerConfig.ACKS_CONFIG, "all");
                }
        )
        );
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

    @Bean(MESSAGE_GET_DEVICES_PROD)
    public KafkaTemplate<String, MessageGetDevices> messageGetDevicesProd() {
        return new KafkaTemplate<>(producerFactory(
                props -> {
                    props.put(ProducerConfig.ACKS_CONFIG, "all");
                })
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
    public ConcurrentKafkaListenerContainerFactory<String, GetCredentials>
    getCredentials() {
        var props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        ConcurrentKafkaListenerContainerFactory<String, GetCredentials>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new JsonDeserializer<>(GetCredentials.class)
                )
        );
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CredentialsAddress> getCredentialsAddress() {
        var props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        ConcurrentKafkaListenerContainerFactory<String, CredentialsAddress>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new JsonDeserializer<>(CredentialsAddress.class)
                )
        );
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, GetCredentialsNewDevice> getCredentialsNewDevice() {
        var props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        ConcurrentKafkaListenerContainerFactory<String, GetCredentialsNewDevice>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new JsonDeserializer<>(GetCredentialsNewDevice.class)
                )
        );
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CredentialsNewDevice> credentialsNewDevice() {
        var props = properties.buildConsumerProperties(null);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        ConcurrentKafkaListenerContainerFactory<String, CredentialsNewDevice>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(
                new DefaultKafkaConsumerFactory<>(
                        props,
                        new StringDeserializer(),
                        new JsonDeserializer<>(CredentialsNewDevice.class)
                )
        );
        return factory;
    }
}
