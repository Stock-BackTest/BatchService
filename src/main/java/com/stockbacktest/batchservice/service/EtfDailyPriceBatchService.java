package com.stockbacktest.batchservice.service;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.stockbacktest.batchservice.dto.EtfDailyPriceApiResponseDto;
import com.stockbacktest.batchservice.dto.EtfDailyPriceDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * ETF 일별 시세 정보
 * https://openapi.krx.co.kr/contents/OPP/USES/service/OPPUSES003_S2.cmd?BO_ID=nrEpCLaZpoLCTzPUMxuF
 */
@Service
public class EtfDailyPriceBatchService {

  @Value("${krx.apiUrl}")
  private String apiUrl;

  @Value("${krx.apikey}")
  private String apiKey;

  /**
   * 기준일자에 대한 ETF들 일별 시세 종목 조회
   *
   * @param baseDate 기준일자
   * @return 기준일자에 대한 ETF들 일별 시세 리스트
   */
  public List<EtfDailyPriceDto> getDailyEtfsPrice(LocalDate baseDate) throws Exception {
    String date = baseDate.format(DateTimeFormatter.BASIC_ISO_DATE);

    RestClient client = RestClient.create(apiUrl);

    EtfDailyPriceApiResponseDto result = client.get()
        .uri(uriBuilder -> uriBuilder
            .queryParam("basDd", date)
            .build())
        .header("AUTH_KEY", apiKey)
        .accept(APPLICATION_JSON)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, (req, rep) -> {
          throw new RuntimeException(rep.getStatusCode().toString());
        })
        .onStatus(HttpStatusCode::is5xxServerError, (req, rep) -> {
          throw new RuntimeException(rep.getStatusCode().toString());
        })
        .body(EtfDailyPriceApiResponseDto.class);

    return Optional.ofNullable(result)
        .orElseThrow(() -> new IllegalStateException("empty response"))
        .etfDailyPriceList();
  }
}