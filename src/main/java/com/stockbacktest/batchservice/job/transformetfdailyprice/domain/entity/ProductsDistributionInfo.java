package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductsDistributionInfo { // 배당금, 분배금

  private String isinCd;
  private LocalDate recordDate;
  private LocalDate payableDate;
  private double cashPerShare;
  private double payoutYieldPct;
  private double taxBasis;
  private String distributionType;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
