package main.manager.output.consumers;

import lombok.extern.slf4j.Slf4j;
import main.manager.output.dto.MessageAuth;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
@Slf4j
public class FromVerifier {
    private final RestTemplate restTemplate;

    public FromVerifier(RestTemplate aRestTemplate) {
        Objects.requireNonNull(aRestTemplate);
        this.restTemplate = aRestTemplate;
    }

    @KafkaListener(
            topics = "verifier-main-manager-output",
            groupId = "verifier-main-manager-output",
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

        if (record.value().getDevices().isEmpty()) {
            log.info("No messages from devices");
            return;
        }

        try {
            // Декодирование адреса
            // Формирование HTTP заголовков
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Формирование HTTP тела запроса
            HttpEntity<MessageAuth> requestEntity = new HttpEntity<>(record.value(), headers);
            log.info("Request to cloud: {}", requestEntity);
            // Отправка POST запроса на сервер
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    record.value().getAddress(),
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            // Проверка статуса ответа
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Success: Received OK status from server");
            } else {
                log.error("Error: Received non-OK status from server: {}", responseEntity.getStatusCode());
            }
        } catch (Throwable e){
            log.info("Error: {}", e.getMessage());
        }


        log.info("End FromVerifier");
    }
}
