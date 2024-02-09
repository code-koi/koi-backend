package com.codekoi.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.codekoi"})
@EntityScan(basePackages = {
        "com.codekoi"
})
public class CodeKoiApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeKoiApiApplication.class, args);
    }
}
