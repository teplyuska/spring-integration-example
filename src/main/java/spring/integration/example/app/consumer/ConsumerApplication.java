package spring.integration.example.app.consumer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAutoConfiguration
@Import(ConsumerApplicationConfig.class)
public class ConsumerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ConsumerApplication.class)
                .web(false)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
