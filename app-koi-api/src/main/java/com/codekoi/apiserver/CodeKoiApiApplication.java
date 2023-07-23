package com.codekoi.apiserver;

import com.codekoi.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.codekoi"})
@EntityScan(basePackageClasses = Persistence.class)
public class CodeKoiApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeKoiApiApplication.class, args);
    }
}
