package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EtfInfo {

  private String isinCd;
  private String managementName;
  private String etfObjIdxName;
  private String taxType;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
}
