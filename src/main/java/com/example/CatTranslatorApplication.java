package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 이게 있어야 데베에 날짜가 저장됨.
public class CatTranslatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatTranslatorApplication.class, args);
    }

}
