package com.stockbacktest.batchservice.job.etfdailypriceapi;

import com.stockbacktest.batchservice.dto.EtfDailyPriceApiResponseDto;
import com.stockbacktest.batchservice.dto.EtfDailyPriceDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestClient;

@Slf4j
@RequiredArgsConstructor
public class EtfDailyPriceApiReader implements ItemReader<EtfDailyPriceDto> {

  private final RestClient restClient;
  private final LocalDate currentDate;
  private Iterator<EtfDailyPriceDto> cursor;

  @Override
  public EtfDailyPriceDto read() {
    // 첫 번째 호출: API 호출하여 Iterator 초기화
    if (cursor == null) {
      try {
        EtfDailyPriceApiResponseDto responseDto = restClient.get()
            .uri(uriBuilder -> uriBuilder
                .queryParam("basDd", currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build())
            .retrieve()
            .body(EtfDailyPriceApiResponseDto.class);

        List<EtfDailyPriceDto> list =
            (responseDto != null && responseDto.etfDailyPriceList() != null)
                ? responseDto.etfDailyPriceList()
                : List.of();

        cursor = list.iterator();
      } catch (RuntimeException e) { //FIXME 추후 Custom Exception으로 변경 필요
        log.error("ETF API 호출 중 에러 발생 - 날짜: {}", currentDate, e);
        log.error(e.getMessage());
      }
    }

    // Iterator에서 다음 요소 반환, 없으면 null (읽기 종료)
    return cursor.hasNext() ? cursor.next() : null;
  }
}
