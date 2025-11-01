package com.stockbacktest.batchservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.stockbacktest.batchservice.dto.EtfDailyPriceDto;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.mock.Expectation;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@ExtendWith(MockServerExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EtfDailyPriceBatchServiceTest {

  private static final String API_URL = "/api-url";
  private static final String API_SERVER_ERROR_URL = "/500-api-url"; // mock에서 500 에러 반환을 테스트하기 위한 url
  private static final String INVALID_API_URL = "/invalid-api-url";
  private static final String API_KEY = "api-key";
  private static final String INVALID_API_KEY = "invalid-api-key";

  private static int mockServerPort;

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @BeforeAll
  void createMockServer(MockServerClient mockServer) throws IOException {
    mockServerPort = mockServer.getPort();

    String body = java.nio.file.Files.readString(
        java.nio.file.Paths.get("src/test/resources/mocks/etf_success_2.json")
    );

    mockServer.upsert(
        new Expectation(
            request()
                .withMethod("GET")
                .withPath(INVALID_API_URL)
                .withQueryStringParameter("basDd", "20200414")
        ).thenRespond(
            response()
                .withStatusCode(404)
        ).withPriority(0)
        , new Expectation(
            request()
                .withMethod("GET")
                .withPath(API_URL)
                .withQueryStringParameter("basDd", "20200414")
        ).thenRespond(
            response()
                .withStatusCode(401)
        ).withPriority(0)
        , new Expectation(
            request()
                .withMethod("GET")
                .withPath(API_SERVER_ERROR_URL)
                .withHeader("AUTH_KEY", API_KEY)
                .withQueryStringParameter("basDd", "20200414")
        ).thenRespond(
            response()
                .withStatusCode(500)
        )
    );

    mockServer.upsert(
        new Expectation(
            request()
                .withMethod("GET")
                .withPath(API_URL)
                .withHeader("AUTH_KEY", API_KEY)
                .withQueryStringParameter("basDd", "20200414")
        ).thenRespond(
            response()
                .withStatusCode(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(body)
        ).withPriority(1),
        new Expectation(
            request()
                .withMethod("GET")
                .withPath(API_URL)
                .withHeader("AUTH_KEY", API_KEY)
                .withQueryStringParameter("basDd", "20200101")
        ).thenRespond(
            response()
                .withStatusCode(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody((String) null)
        ).withPriority(1)
    );
  }

  @Test
  @DisplayName("API URL이 NULL이면 IllegalArgumentException이 throw 된다")
  void Given_Null_ApiURL_When_GetDailyEtfsPrice_Then_Throw_NullPointerException(
      MockServerClient mockServerClient) throws Exception {
    // Arrange
    EtfDailyPriceBatchService service = new EtfDailyPriceBatchService();

    setField(service, "apiUrl", null);
    setField(service, "apiKey", API_KEY);

    // Arrange & Assert
    assertThatThrownBy(() -> service.getDailyEtfsPrice(LocalDate.of(2020, 4, 14)))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("URI with undefined scheme");
  }

  @Test
  @DisplayName("API키가 NULL이면 NullPointerException이 throw 된다")
  void Given_Null_Apikey_When_GetDailyEtfsPrice_Then_Throw_NullPointerException(
      MockServerClient mockServerClient) throws Exception {
    // Arrange
    EtfDailyPriceBatchService service = new EtfDailyPriceBatchService();

    setField(service, "apiUrl", "http://localhost:" + mockServerPort + API_URL);
    setField(service, "apiKey", null);

    // Arrange & Assert
    assertThatThrownBy(() -> service.getDailyEtfsPrice(LocalDate.of(2020, 4, 14)))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("API URL이 유효하지 않으면 RuntimeException이 throw 된다")
  void Given_Invalid_ApiURL_When_GetDailyEtfsPrice_Then_Throw_RuntimeException(
      MockServerClient mockServerClient) throws Exception {
    // Arrange
    EtfDailyPriceBatchService service = new EtfDailyPriceBatchService();

    setField(service, "apiUrl", "http://localhost:" + mockServerPort + INVALID_API_URL);
    setField(service, "apiKey", API_KEY);

    // Arrange & Assert
    assertThatThrownBy(() -> service.getDailyEtfsPrice(LocalDate.of(2020, 4, 14)))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("404");
  }

  @Test
  @DisplayName("API KEY가 유효하지 않으면 RuntimeException이 throw 된다")
  void Given_Invalid_Apikey_When_GetDailyEtfsPrice_Then_Throw_RuntimeException(
      MockServerClient mockServerClient) throws Exception {
    // Arrange
    EtfDailyPriceBatchService service = new EtfDailyPriceBatchService();

    setField(service, "apiUrl", "http://localhost:" + mockServerPort + API_URL);
    setField(service, "apiKey", INVALID_API_KEY);

    // Arrange & Assert
    assertThatThrownBy(() -> service.getDailyEtfsPrice(LocalDate.of(2020, 4, 14)))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("401");
  }

  @Test
  @DisplayName("getDailyEtfsPrice 인자로 null을 넘기면 NullPointerException이 throw 된다")
  void Given_Null_Parameter_When_GetDailyEtfsPrice_Then_Throw_RuntimeException(
      MockServerClient mockServerClient) throws Exception {
    // Arrange
    EtfDailyPriceBatchService service = new EtfDailyPriceBatchService();

    setField(service, "apiUrl", "http://localhost:" + mockServerPort + API_URL);
    setField(service, "apiKey", API_KEY);

    // Arrange & Assert
    assertThatThrownBy(() -> service.getDailyEtfsPrice(null))
        .isInstanceOf(NullPointerException.class);
  }

  @Test
  @DisplayName("API가 500에러를 반환하면 RuntimeException이 throw 된다")
  void Given_500_Error_When_GetDailyEtfsPrice_Then_Throw_RuntimeException(
      MockServerClient mockServerClient) throws Exception {
    // Arrange
    EtfDailyPriceBatchService service = new EtfDailyPriceBatchService();

    setField(service, "apiUrl", "http://localhost:" + mockServerPort + API_SERVER_ERROR_URL);
    setField(service, "apiKey", API_KEY);

    // Arrange & Assert
    assertThatThrownBy(() -> service.getDailyEtfsPrice(LocalDate.of(2020, 4, 14)))
        .isInstanceOf(RuntimeException.class)
        .hasMessageContaining("500");
  }

  @Test
  @DisplayName("API가 null을 반환하면 IllegalStateException이 throw 된다")
  void Given_ApiReturnsNull_When_GetDailyEtfsPrice_Then_Throw_IllegalStateException(
      MockServerClient mockServerClient) throws Exception {
    // Arrange
    EtfDailyPriceBatchService service = new EtfDailyPriceBatchService();

    setField(service, "apiUrl", "http://localhost:" + mockServerPort + API_URL);
    setField(service, "apiKey", API_KEY);

    // Arrange & Assert
    assertThatThrownBy(() -> service.getDailyEtfsPrice(LocalDate.of(2020, 1, 1)))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("empty response");
  }

  @Test
  @DisplayName("API 정상 호출되면 etf 시세 리스트를 정상적으로 가져온다")
  void Given_Valid_Request_When_GetDailyEtfsPrice_Then_Returns_EtfPriceList(
      MockServerClient mockServerClient) throws Exception {
    // Arrange
    EtfDailyPriceBatchService service = new EtfDailyPriceBatchService();

    setField(service, "apiUrl", "http://localhost:" + mockServerPort + API_URL);
    setField(service, "apiKey", API_KEY);

    // Arrange & Assert
    List<EtfDailyPriceDto> result = service.getDailyEtfsPrice(LocalDate.of(2020, 4, 14));

    assertThat(result.size()).isEqualTo(2);
    assertThat(result.getFirst().isinShortCode()).isEqualTo("253150");
  }
}