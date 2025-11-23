package com.stockbacktest.batchservice.job.etfdailypriceapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.stockbacktest.batchservice.dto.EtfDailyPriceDto;
import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EtfDailyPriceApiProcessorTest {

  private final EtfDailyPriceApiProcessor processor = new EtfDailyPriceApiProcessor();

  @Test
  @DisplayName("유효한 DTO를 넘기면 Entity로 정상 변환된다")
  void Given_Valid_Dto_When_Process_Then_Returns_Entity() {
    // Given
    EtfDailyPriceDto dto = new EtfDailyPriceDto(
        "20251030",
        "069500",
        "KODEX 200",
        "35000",
        "100",
        "0.29",
        "34500.0",
        "34900",
        "35100",
        "34800",
        "1000000",
        "35000000000",
        "1500000000000",
        "1480000000000",
        "42000000",
        "KOSPI 200",
        "350.5",
        "1.2",
        "0.34"
    );

    // When
    EtfDailyPrice result = processor.process(dto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getBaseDate()).isEqualTo("20251030");
    assertThat(result.getIsinShortCode()).isEqualTo("069500");
    assertThat(result.getItemsName()).isEqualTo("KODEX 200");
    assertThat(result.getClosePrice()).isEqualTo("35000");
    assertThat(result.getDiffFromPrevPrice()).isEqualTo("100");
    assertThat(result.getFluctuationRate()).isEqualTo("0.29");
    assertThat(result.getNav()).isEqualTo("34500.0");
    assertThat(result.getOpenPrice()).isEqualTo("34900");
    assertThat(result.getHighPrice()).isEqualTo("35100");
    assertThat(result.getLowPrice()).isEqualTo("34800");
    assertThat(result.getAccumulatedVolume()).isEqualTo("1000000");
    assertThat(result.getAccumulatedTradeValue()).isEqualTo("35000000000");
    assertThat(result.getMarketCap()).isEqualTo("1500000000000");
    assertThat(result.getTotalNetAsset()).isEqualTo("1480000000000");
    assertThat(result.getListedShares()).isEqualTo("42000000");
    assertThat(result.getIndexName()).isEqualTo("KOSPI 200");
    assertThat(result.getIndexClose()).isEqualTo("350.5");
    assertThat(result.getIndexDiffFromPrev()).isEqualTo("1.2");
    assertThat(result.getIndexFluctuationRate()).isEqualTo("0.34");
  }

  @Test
  @DisplayName("NULL 값이 포함된 DTO를 넘기면 NULL이 하이픈으로 변환된다")
  void Given_Dto_With_Null_Values_When_Process_Then_Null_Converts_To_Hyphen() {
    // Given
    EtfDailyPriceDto dto = new EtfDailyPriceDto(
        "20251030",
        "069500",
        "KODEX 200",
        "35000",
        "100",
        "0.29",
        null,  // NAV가 null
        "34900",
        "35100",
        "34800",
        "1000000",
        "35000000000",
        "1500000000000",
        "1480000000000",
        "42000000",
        "KOSPI 200",
        null,  // indexClose가 null
        "1.2",
        "0.34"
    );

    // When
    EtfDailyPrice result = processor.process(dto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getNav()).isEqualTo("-");
    assertThat(result.getIndexClose()).isEqualTo("-");
    assertThat(result.getBaseDate()).isEqualTo("20251030");
    assertThat(result.getIsinShortCode()).isEqualTo("069500");
  }

  @Test
  @DisplayName("빈 문자열이 포함된 DTO를 넘기면 빈 문자열이 하이픈으로 변환된다")
  void Given_Dto_With_Empty_Strings_When_Process_Then_Empty_Strings_Convert_To_Hyphen()
  {
    // Given
    EtfDailyPriceDto dto = new EtfDailyPriceDto(
        "20251030",
        "069500",
        "KODEX 200",
        "  ",  // 빈 문자열
        "",  // 빈 문자열
        "0.29",
        "34500.0",
        "34900",
        "35100",
        "34800",
        "1000000",
        "35000000000",
        "1500000000000",
        "1480000000000",
        "42000000",
        "KOSPI 200",
        "350.5",
        "1.2",
        "0.34"
    );

    // When
    EtfDailyPrice result = processor.process(dto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getClosePrice()).isEqualTo("-");
    assertThat(result.getDiffFromPrevPrice()).isEqualTo("-");
  }

  @Test
  @DisplayName("NULL과 빈 문자열이 섞인 DTO를 넘기면 모두 하이픈으로 변환된다")
  void Given_Dto_With_Null_And_Empty_Strings_When_Process_Then_All_Convert_To_Hyphen()
  {
    // Given
    EtfDailyPriceDto dto = new EtfDailyPriceDto(
        "20251030",
        "069500",
        "KODEX 200",
        null,  // null
        "  ",    // 빈 문자열
        "0.29",
        null,  // null
        "",    // 빈 문자열
        "35100",
        "34800",
        "1000000",
        "35000000000",
        "1500000000000",
        "1480000000000",
        "42000000",
        "KOSPI 200",
        "350.5",
        "1.2",
        "0.34"
    );

    // When
    EtfDailyPrice result = processor.process(dto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getClosePrice()).isEqualTo("-");
    assertThat(result.getDiffFromPrevPrice()).isEqualTo("-");
    assertThat(result.getNav()).isEqualTo("-");
    assertThat(result.getOpenPrice()).isEqualTo("-");
  }

  @Test
  @DisplayName("음수 문자열이 포함된 DTO를 넘기면 음수가 그대로 유지된다")
  void Given_Dto_With_Negative_Values_When_Process_Then_Negative_Values_Remain() {
    // Given
    EtfDailyPriceDto dto = new EtfDailyPriceDto(
        "20251030",
        "069500",
        "KODEX 200",
        "35000",
        "-100",  // 음수
        "-0.29",  // 음수
        "34500.0",
        "34900",
        "35100",
        "34800",
        "1000000",
        "35000000000",
        "1500000000000",
        "1480000000000",
        "42000000",
        "KOSPI 200",
        "350.5",
        "-1.2",  // 음수
        "-0.34"  // 음수
    );

    // When
    EtfDailyPrice result = processor.process(dto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getDiffFromPrevPrice()).isEqualTo("-100");
    assertThat(result.getFluctuationRate()).isEqualTo("-0.29");
    assertThat(result.getIndexDiffFromPrev()).isEqualTo("-1.2");
    assertThat(result.getIndexFluctuationRate()).isEqualTo("-0.34");
  }

  @Test
  @DisplayName("NULL인 DTO를 넘기면 NullPointerException이 throw 된다")
  void Given_Null_Dto_When_Process_Then_Throw_NullPointerException() {
    // Given & When & Then
    assertThatThrownBy(() -> processor.process(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("하이픈 문자열이 포함된 DTO를 넘기면 하이픈이 그대로 유지된다")
  void Given_Dto_With_Hyphen_String_When_Process_Then_Hyphen_Remains() {
    // Given
    EtfDailyPriceDto dto = new EtfDailyPriceDto(
        "20251030",
        "069500",
        "KODEX 200",
        "35000",
        "100",
        "-",  // 하이픈 문자열
        "-",  // 하이픈 문자열
        "34900",
        "35100",
        "34800",
        "1000000",
        "35000000000",
        "1500000000000",
        "1480000000000",
        "42000000",
        "KOSPI 200",
        "-",  // 하이픈 문자열
        "1.2",
        "0.34"
    );

    // When
    EtfDailyPrice result = processor.process(dto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getFluctuationRate()).isEqualTo("-");
    assertThat(result.getNav()).isEqualTo("-");
    assertThat(result.getIndexClose()).isEqualTo("-");
  }

  @Test
  @DisplayName("NULL과 유효값이 섞인 DTO를 넘기면 NULL만 하이픈으로 변환된다")
  void Given_Dto_With_Mixed_Null_And_Valid_When_Process_Then_Only_Null_Converts_To_Hyphen() {
    // Given
    EtfDailyPriceDto dto = new EtfDailyPriceDto(
        "20251030",
        null,  // null
        "KODEX 200",
        "35000",
        null,  // null
        "0.29",
        null,  // null
        "34900",
        null,  // null
        "34800",
        "1000000",
        null,  // null
        "1500000000000",
        null,  // null
        "42000000",
        "KOSPI 200",
        null,  // null
        "1.2",
        null   // null
    );

    // When
    EtfDailyPrice result = processor.process(dto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getBaseDate()).isEqualTo("20251030");
    assertThat(result.getIsinShortCode()).isEqualTo("-");
    assertThat(result.getItemsName()).isEqualTo("KODEX 200");
    assertThat(result.getClosePrice()).isEqualTo("35000");
    assertThat(result.getDiffFromPrevPrice()).isEqualTo("-");
    assertThat(result.getFluctuationRate()).isEqualTo("0.29");
    assertThat(result.getNav()).isEqualTo("-");
    assertThat(result.getOpenPrice()).isEqualTo("34900");
    assertThat(result.getHighPrice()).isEqualTo("-");
    assertThat(result.getLowPrice()).isEqualTo("34800");
    assertThat(result.getAccumulatedVolume()).isEqualTo("1000000");
    assertThat(result.getAccumulatedTradeValue()).isEqualTo("-");
    assertThat(result.getMarketCap()).isEqualTo("1500000000000");
    assertThat(result.getTotalNetAsset()).isEqualTo("-");
    assertThat(result.getListedShares()).isEqualTo("42000000");
    assertThat(result.getIndexName()).isEqualTo("KOSPI 200");
    assertThat(result.getIndexClose()).isEqualTo("-");
    assertThat(result.getIndexDiffFromPrev()).isEqualTo("1.2");
    assertThat(result.getIndexFluctuationRate()).isEqualTo("-");
  }
}