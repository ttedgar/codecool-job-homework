package com.codecool.codecooljobhomework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class CodecoolJobHomeworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodecoolJobHomeworkApplication.class, args);
    }

}
