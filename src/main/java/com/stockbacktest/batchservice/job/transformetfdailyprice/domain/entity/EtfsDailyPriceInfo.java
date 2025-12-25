package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EtfsDailyPriceInfo {

  private String isinCd;
  private LocalDate baseDate;
  private Long totalNetAssets;
  private Long nav;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
