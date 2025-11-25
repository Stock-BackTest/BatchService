package com.stockbacktest.batchservice.job.etfdailypriceapi;

import com.stockbacktest.batchservice.dto.EtfDailyPriceDto;
import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import lombok.NonNull;
import org.springframework.batch.item.ItemProcessor;

public class EtfDailyPriceApiProcessor implements
    ItemProcessor<EtfDailyPriceDto, EtfDailyPrice> {

  @Override
  public EtfDailyPrice process(@NonNull EtfDailyPriceDto dto) {
    return EtfDailyPrice.builder()
        .baseDate(nullOrEmptyStringToHyphen(dto.baseDate()))
        .isinShortCode(nullOrEmptyStringToHyphen(dto.isinShortCode()))
        .itemsName(nullOrEmptyStringToHyphen(dto.itemsName()))
        .closePrice(nullOrEmptyStringToHyphen(dto.closePrice()))
        .diffFromPrevPrice(nullOrEmptyStringToHyphen(dto.diffFromPrevPrice()))
        .fluctuationRate(nullOrEmptyStringToHyphen(dto.fluctuationRate()))
        .nav(nullOrEmptyStringToHyphen(dto.nav()))
        .openPrice(nullOrEmptyStringToHyphen(dto.openPrice()))
        .highPrice(nullOrEmptyStringToHyphen(dto.highPrice()))
        .lowPrice(nullOrEmptyStringToHyphen(dto.lowPrice()))
        .accumulatedVolume(nullOrEmptyStringToHyphen(dto.accumulatedVolume()))
        .accumulatedTradeValue(nullOrEmptyStringToHyphen(dto.accumulatedTradeValue()))
        .marketCap(nullOrEmptyStringToHyphen(dto.marketCap()))
        .totalNetAsset(nullOrEmptyStringToHyphen(dto.totalNetAsset()))
        .listedShares(nullOrEmptyStringToHyphen(dto.listedShares()))
        .indexName(nullOrEmptyStringToHyphen(dto.indexName()))
        .indexClose(nullOrEmptyStringToHyphen(dto.indexClose()))
        .indexDiffFromPrev(nullOrEmptyStringToHyphen(dto.indexDiffFromPrev()))
        .indexFluctuationRate(nullOrEmptyStringToHyphen(dto.indexFluctuationRate()))
        .build();
  }

  /**
   * null 또는 빈 문자열을 "-"로 변환
   */
  private String nullOrEmptyStringToHyphen(String value) {
    if (value == null || value.isBlank()) {
      return "-";
    }
    return value;
  }
}
