package com.stockbacktest.batchservice.job.etfdailypriceapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.stockbacktest.batchservice.dto.EtfDailyPriceDto;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.mock.Expectation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@ExtendWith(MockServerExtension.class)
class EtfDailyPriceApiReaderTest {

  private static final String API_PATH = "/etf-daily-price";
  private static final String ERROR_API_PATH = "/error-api";

  private int mockServerPort;

  @BeforeEach
  void setUp(MockServerClient mockServer) throws IOException {
    mockServerPort = mockServer.getPort();

    // 정상 응답 (3개 데이터)
    String successBody = java.nio.file.Files.readString(
        java.nio.file.Paths.get("src/test/resources/mocks/etf_api_success_3.json")
    );

    // 빈 응답
    String emptyBody = java.nio.file.Files.readString(
        java.nio.file.Paths.get("src/test/resources/mocks/etf_api_empty.json")
    );

    // null 필드 포함 응답
    String nullFieldsBody = java.nio.file.Files.readString(
        java.nio.file.Paths.get("src/test/resources/mocks/etf_api_with_null_fields.json")
    );

    // Mock 설정
    mockServer.upsert(
        // 정상 응답
        new Expectation(
            request()
                .withMethod("GET")
                .withPath(API_PATH)
                .withQueryStringParameter("basDd", "20251030")
        ).thenRespond(
            response()
                .withStatusCode(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(successBody)
        ).withPriority(1),

        // 빈 응답
        new Expectation(
            request()
                .withMethod("GET")
                .withPath(API_PATH)
                .withQueryStringParameter("basDd", "20200101")
        ).thenRespond(
            response()
                .withStatusCode(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(emptyBody)
        ).withPriority(1),

        // null 필드 포함 응답
        new Expectation(
            request()
                .withMethod("GET")
                .withPath(API_PATH)
                .withQueryStringParameter("basDd", "20200414")
        ).thenRespond(
            response()
                .withStatusCode(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(nullFieldsBody)
        ).withPriority(1),

        // null body 응답
        new Expectation(
            request()
                .withMethod("GET")
                .withPath(API_PATH)
                .withQueryStringParameter("basDd", "20200102")
        ).thenRespond(
            response()
                .withStatusCode(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody((String) null)
        ).withPriority(1),

        // 401 에러
        new Expectation(
            request()
                .withMethod("GET")
                .withPath(API_PATH)
                .withQueryStringParameter("basDd", "20230101")
        ).thenRespond(
            response()
                .withStatusCode(401)
        ).withPriority(0),

        // 404 에러
        new Expectation(
            request()
                .withMethod("GET")
                .withPath(ERROR_API_PATH)
                .withQueryStringParameter("basDd", "20251030")
        ).thenRespond(
            response()
                .withStatusCode(404)
        ).withPriority(0),

        // 500 에러
        new Expectation(
            request()
                .withMethod("GET")
                .withPath(API_PATH)
                .withQueryStringParameter("basDd", "20250101")
        ).thenRespond(
            response()
                .withStatusCode(500)
        ).withPriority(0)
    );
  }

  @Test
  @DisplayName("API가 정상 응답하면 데이터를 순차적으로 읽는다")
  void Given_Valid_Api_Response_When_Read_Then_Returns_Data_Sequentially() {
    // Given
    RestClient restClient = RestClient.builder()
        .baseUrl("http://localhost:" + mockServerPort + API_PATH)
        .build();
    EtfDailyPriceApiReader reader = new EtfDailyPriceApiReader(
        restClient,
        LocalDate.of(2025, 10, 30)
    );

    // 기대하는 데이터 정의
    String[][] expectedData = {
        {"069500", "KODEX 200"},
        {"069660", "KODEX 레버리지"},
        {"114800", "KODEX 인버스"}
    };

    // When & Then
    for (String[] expected : expectedData) {
      EtfDailyPriceDto item = reader.read();
      assertThat(item).isNotNull();
      assertThat(item.isinShortCode()).isEqualTo(expected[0]);
      assertThat(item.itemsName()).isEqualTo(expected[1]);
    }

    // 더 이상 데이터 없음 확인
    assertThat(reader.read()).isNull();
  }

  @Test
  @DisplayName("API가 빈 리스트를 응답하면 첫 read()에서 null을 반환한다")
  void Given_Empty_Api_Response_When_Read_Then_Returns_Null() {
    // Given
    RestClient restClient = RestClient.builder()
        .baseUrl("http://localhost:" + mockServerPort + API_PATH)
        .build();
    EtfDailyPriceApiReader reader = new EtfDailyPriceApiReader(
        restClient,
        LocalDate.of(2020, 1, 1)
    );

    // When
    EtfDailyPriceDto item = reader.read();

    // Then
    assertThat(item).isNull();
  }

  @Test
  @DisplayName("API가 null body를 응답하면 첫 read()에서 null을 반환한다")
  void Given_Null_Body_Response_When_Read_Then_Returns_Null() {
    // Given
    RestClient restClient = RestClient.builder()
        .baseUrl("http://localhost:" + mockServerPort + API_PATH)
        .build();
    EtfDailyPriceApiReader reader = new EtfDailyPriceApiReader(
        restClient,
        LocalDate.of(2020, 1, 2)
    );

    // When
    EtfDailyPriceDto item = reader.read();

    // Then
    assertThat(item).isNull();
  }

  @Test
  @DisplayName("API가 null 필드를 포함한 응답을 하면 데이터를 정상 읽는다")
  void Given_Response_With_Null_Fields_When_Read_Then_Returns_Data_With_Nulls() {
    // Given
    RestClient restClient = RestClient.builder()
        .baseUrl("http://localhost:" + mockServerPort + API_PATH)
        .build();
    EtfDailyPriceApiReader reader = new EtfDailyPriceApiReader(
        restClient,
        LocalDate.of(2020, 4, 14)
    );

    // When
    EtfDailyPriceDto item = reader.read();

    // Then
    assertThat(item).isNotNull();
    assertThat(item.isinShortCode()).isEqualTo("069500");
    assertThat(item.diffFromPrevPrice()).isEmpty();
    assertThat(item.fluctuationRate()).isEqualTo("-");
    assertThat(item.nav()).isNull();
    assertThat(item.accumulatedTradeValue()).isEmpty();
    assertThat(item.totalNetAsset()).isNull();
    assertThat(item.indexClose()).isNull();
  }

  @Test
  @DisplayName("API가 401 에러를 반환하면 RuntimeException이 throw 된다")
  void Given_401_Error_When_Read_Then_Throw_RuntimeException() {
    // Given
    RestClient restClient = RestClient.builder()
        .baseUrl("http://localhost:" + mockServerPort + API_PATH)
        .build();
    EtfDailyPriceApiReader reader = new EtfDailyPriceApiReader(
        restClient,
        LocalDate.of(2023, 1, 1)
    );

    // When & Then
    assertThatThrownBy(reader::read)
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("API가 404 에러를 반환하면 RuntimeException이 throw 된다")
  void Given_404_Error_When_Read_Then_Throw_RuntimeException() {
    // Given
    RestClient restClient = RestClient.builder()
        .baseUrl("http://localhost:" + mockServerPort + ERROR_API_PATH)
        .build();
    EtfDailyPriceApiReader reader = new EtfDailyPriceApiReader(
        restClient,
        LocalDate.of(2025, 10, 30)
    );

    // When & Then
    assertThatThrownBy(reader::read)
        .isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("API가 500 에러를 반환하면 RuntimeException이 throw 된다")
  void Given_500_Error_When_Read_Then_Throw_RuntimeException() {
    // Given
    RestClient restClient = RestClient.builder()
        .baseUrl("http://localhost:" + mockServerPort + API_PATH)
        .build();
    EtfDailyPriceApiReader reader = new EtfDailyPriceApiReader(
        restClient,
        LocalDate.of(2025, 1, 1)
    );

    // When & Then
    assertThatThrownBy(reader::read)
        .isInstanceOf(RuntimeException.class);
  }
}