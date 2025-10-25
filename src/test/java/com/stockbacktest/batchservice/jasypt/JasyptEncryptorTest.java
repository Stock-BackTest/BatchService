package com.stockbacktest.batchservice.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JasyptEncryptorTest {

  @Autowired
  private StringEncryptor stringEncryptor;

  @Test
  @DisplayName("Encrypt Test")
  void encryptSamples() {
    String plainText = "ThisIsTestSecretKey";
    String encryptedText = stringEncryptor.encrypt(plainText);
    String decryptedText = stringEncryptor.decrypt(encryptedText);

    System.out.println("Encrypted = ENC(" + encryptedText + ")");

    Assertions.assertEquals(plainText, decryptedText);
  }
}
