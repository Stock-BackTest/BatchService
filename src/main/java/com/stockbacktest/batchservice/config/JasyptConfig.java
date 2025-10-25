package com.stockbacktest.batchservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@Configuration
public class JasyptConfig {

  @Value("${jasypt.encryptor.password}")
  private String password;

  @Bean("jasyptStringEncryptor")
  @Primary
  public StringEncryptor stringEncryptor() {
    // 동시성(멀티스레드) 대응 가능한 encryptor
    PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

    // 단일 암호화 설정 객체
    SimpleStringPBEConfig config = new SimpleStringPBEConfig();

    //암호화 키: 환경변수로 주입
    config.setPassword(password);

    //암호화 알고리즘
    config.setAlgorithm("PBEWithHmacSHA256AndAES_256");

    // 반복 횟수 (Key stretching iterations): PBKDF2 방식으로 암호화 키를 생성할 때, 몇 번의 반복 연산을 수행할지를 결정
    config.setKeyObtentionIterations("10000");

    // 풀 크기: 동시에 몇 개의 Cipher 인스턴스를 돌릴지 지정
    config.setPoolSize("4");

    //보안 제공자: 암호화 연산을 실제로 수행하는 JCE Provider 이름
    config.setProviderName("SunJCE");

    //Salt 생성기: RandomSaltGenerator는 요청 시마다 랜덤 Salt를 자동 생성
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");

    //IV(Initialization Vector) 생성기: RandomIvGenerator 사용으로 CBC 모드 시 재현 불가능한 암호문 보장
    config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");

    //암호화 결과 포맷
    config.setStringOutputType("base64");

    encryptor.setConfig(config);
    return encryptor;
  }
}