package com.stockbacktest.batchservice.jasypt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.stockbacktest.batchservice.config.JasyptConfig;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
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
  @DisplayName("성공 - 암호화 및 복호화 성공")
  void encryptAndDecrypt_Success() {
    // given
    String plainText = "ThisIsTestSecretKey";

    // when
    String encrypted = stringEncryptor.encrypt(plainText);
    String decrypted = stringEncryptor.decrypt(encrypted);

    // then
    assertThat(encrypted)
        .isNotEqualTo(plainText);

    assertThat(decrypted)
        .isEqualTo(plainText);
  }

  @Test
  @DisplayName("실패 - 잘못된 암호문 복호화 시 예외 발생")
  void decrypt_InvalidCipher_Fail() {
    // given
    String invalidCipher = "invalid-encrypted-text";

    // when & then
    assertThatThrownBy(() -> stringEncryptor.decrypt(invalidCipher))
        .isInstanceOf(EncryptionOperationNotPossibleException.class);
  }
}
