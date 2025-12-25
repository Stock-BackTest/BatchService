package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductsDailyPriceInfo {

  private String isinCd;
  private LocalDate baseDate;
  private Integer mrktPrice;
  private Integer highestPrice;
  private Integer lowestPrice;
  private Integer closePrice;
  private Integer tradingQuantity;
  private Long tradingPrice;
  private Long sharesOutstanding;
  private Long mrktTotalAmount;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
