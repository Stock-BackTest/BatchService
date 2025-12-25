package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class Dividends {
  private String isinCd;
  private LocalDate baseDate;
  private LocalDate actualPayDate;
  private String divType;
  private double distPerShare;
  private double taxStd;
  private double estmStdPrc;
}
