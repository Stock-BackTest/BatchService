package com.stockbacktest.batchservice.job.etfdailypriceapi;

import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.mapper.EtfDailyPriceMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@RequiredArgsConstructor
public class EtfDailyPriceApiWriter implements ItemWriter<EtfDailyPrice> {

  private final EtfDailyPriceMapper etfDailyPriceMapper;

  @Override
  public void write(Chunk<? extends EtfDailyPrice> chunk) {
    List<EtfDailyPrice> etfDailyPrices = new ArrayList<>(chunk.getItems());
    
    if (!etfDailyPrices.isEmpty()) {
      etfDailyPriceMapper.insertDailyPrice(etfDailyPrices);
    }
  }
}
