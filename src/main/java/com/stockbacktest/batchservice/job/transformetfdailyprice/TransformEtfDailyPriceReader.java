package com.stockbacktest.batchservice.job.transformetfdailyprice;

import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.mapper.EtfDailyPriceMapper;
import java.time.LocalDate;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

@RequiredArgsConstructor
public class TransformEtfDailyPriceReader implements ItemReader<EtfDailyPrice> {

  private final EtfDailyPriceMapper etfDailyPriceMapper;
  private Iterator<EtfDailyPrice> cursor = null;

  @Override
  public EtfDailyPrice read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if (cursor == null) {
      LocalDate now = LocalDate.now();
      cursor = etfDailyPriceMapper.findByBaseDate(now).iterator();
    }
    return cursor.hasNext() ? cursor.next() : null;
  }
}
