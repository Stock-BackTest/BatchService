package com.stockbacktest.batchservice.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  private static final String AUTH_KEY = "AUTH_KEY";

  @Value("${krx.apiUrl}")
  private String apiUrl;

  @Value("${krx.apikey}")
  private String apiKey;

  @Bean
  public RestClient restClient() {
    return RestClient.builder()
        .baseUrl(apiUrl)
        .requestFactory(requestFactory())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(AUTH_KEY, apiKey)
        .defaultStatusHandler(
            HttpStatusCode::isError, (request, response) -> {
              if(response.getStatusCode().is4xxClientError()) throw new RuntimeException("4xx Client Error");
              if(response.getStatusCode().is5xxServerError()) throw new RuntimeException("5xx Server Error");
            }
        )
        .build();
  }

  private SimpleClientHttpRequestFactory requestFactory() {
    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setConnectTimeout(Duration.ofSeconds(1));
    requestFactory.setReadTimeout(Duration.ofSeconds(30));

    return requestFactory;
  }
}
