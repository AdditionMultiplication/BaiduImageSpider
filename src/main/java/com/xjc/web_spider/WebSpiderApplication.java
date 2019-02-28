package com.xjc.web_spider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.xjc")
public class WebSpiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSpiderApplication.class, args);
    }

}
