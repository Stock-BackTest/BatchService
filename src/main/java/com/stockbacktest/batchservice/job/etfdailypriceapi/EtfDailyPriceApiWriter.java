package com.stockbacktest.batchservice.job.etfdailypriceapi;

import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.mapper.EtfDailyPriceMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
@RequiredArgsConstructor
public class EtfDailyPriceApiWriter implements ItemWriter<EtfDailyPrice> {

  private final EtfDailyPriceMapper etfDailyPriceMapper;

  @Override
  public void write(Chunk<? extends EtfDailyPrice> chunk) {
    List<EtfDailyPrice> etfDailyPrices = new ArrayList<>(chunk.getItems());
    
    if (!etfDailyPrices.isEmpty()) {
      try {
        etfDailyPriceMapper.insertDailyPrice(etfDailyPrices);
      } catch (RuntimeException e) { //FIXME 추후 Custom Exception으로 변경 필요
        log.error("ETF 데이터 저장 중 에러 발생", e);
        log.error(e.getMessage());
      }
    }
  }
}
