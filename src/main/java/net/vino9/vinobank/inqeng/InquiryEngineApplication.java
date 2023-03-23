package net.vino9.vinobank.inqeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class InquiryEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(InquiryEngineApplication.class, args);
    }

}
