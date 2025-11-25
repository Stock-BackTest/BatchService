package com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EtfDailyPrice {
  private String baseDate;
  private String isinShortCode;
  private String itemsName;
  private String closePrice;
  private String diffFromPrevPrice;
  private String fluctuationRate;
  private String nav;
  private String openPrice;
  private String highPrice;
  private String lowPrice;
  private String accumulatedVolume;
  private String accumulatedTradeValue;
  private String marketCap;
  private String totalNetAsset;
  private String listedShares;
  private String indexName;
  private String indexClose;
  private String indexDiffFromPrev;
  private String indexFluctuationRate;
}
