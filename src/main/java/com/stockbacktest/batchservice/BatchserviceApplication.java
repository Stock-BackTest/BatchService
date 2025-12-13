package com.stockbacktest.batchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BatchserviceApplication {

  public static void main(String[] args) {
    SpringApplication.run(BatchserviceApplication.class, args);
  }
}
