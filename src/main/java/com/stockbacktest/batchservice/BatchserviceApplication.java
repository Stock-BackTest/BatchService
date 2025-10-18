package com.stockbacktest.batchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: DB 인스턴스가 추가되면 exclude 옵션 제거
@SpringBootApplication(exclude = { org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class })
public class BatchserviceApplication {

  public static void main(String[] args) {
    SpringApplication.run(BatchserviceApplication.class, args);
  }
}
