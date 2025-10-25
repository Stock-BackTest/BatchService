package com.stockbacktest.batchservice.jasypt;

import com.stockbacktest.batchservice.config.JasyptConfig;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JasyptConfig.class})
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
