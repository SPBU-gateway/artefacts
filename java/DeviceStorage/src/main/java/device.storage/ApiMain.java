package device.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@Slf4j
public class ApiMain {
    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args the command-line arguments
     */
    public static void main(final String[] args) {
        log.info("Start spring boot by asavershin");
        SpringApplication.run(ApiMain.class, args);
    }

    private void foo() {
        throw new UnsupportedOperationException();
    }
}
