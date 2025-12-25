package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SecuritiesProductsInfo {

  private String isinCd;
  private String isinShrtCd;
  private String securityName;
  private String assetCd;
  private LocalDate listingDate;
  private LocalDate delistingDate;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
