package com.stockbacktest.batchservice.config;

import org.springframework.context.annotation.Configuration;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@Configuration
public class JasyptConfig {

  @Bean("jasyptStringEncryptor")
  @Primary
  public StringEncryptor stringEncryptor() {
    // 동시성(멀티스레드) 대응 가능한 encryptor
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

    SimpleStringPBEConfig config = new SimpleStringPBEConfig();

    config.setPassword(System.getenv("JASYPT_ENCRYPTOR_PASSWORD"));
    config.setAlgorithm("PBEWithHmacSHA256AndAES_256");
    config.setKeyObtentionIterations("10000");
    config.setPoolSize("4");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
    config.setStringOutputType("base64");

    encryptor.setConfig(config);
    return encryptor;
  }
}