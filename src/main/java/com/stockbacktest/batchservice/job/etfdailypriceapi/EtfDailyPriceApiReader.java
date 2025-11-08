package com.stockbacktest.batchservice.job.etfdailypriceapi;

import com.stockbacktest.batchservice.dto.EtfDailyPriceApiResponseDto;
import com.stockbacktest.batchservice.dto.EtfDailyPriceDto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestClient;

@RequiredArgsConstructor
public class EtfDailyPriceApiReader implements ItemReader<EtfDailyPriceDto> {

  private final RestClient restClient;
  private final LocalDate currentDate;
  private Iterator<EtfDailyPriceDto> cursor;

  @Override
  public EtfDailyPriceDto read() {
    // 첫 번째 호출: API 호출하여 Iterator 초기화
    if (cursor == null) {
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
    }

    // Iterator에서 다음 요소 반환, 없으면 null (읽기 종료)
    return cursor.hasNext() ? cursor.next() : null;
  }
}
