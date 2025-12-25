package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TransformEtfDailyPrice {
  private EtfInfo etfInfo;
  private EtfsDailyPriceInfo etfsDailyPriceInfo;
  private ProductsDailyPriceInfo productsDailyPriceInfo;
  private List<ProductsDistributionInfo> productsDistributionInfo;
}
