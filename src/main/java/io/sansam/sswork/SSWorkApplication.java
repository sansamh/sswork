package io.sansam.sswork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableTransactionManagement
@RestController
public class SSWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSWorkApplication.class, args);
    }

    @GetMapping("/")
    public String index() {
        return "Hello World";
    }

}
