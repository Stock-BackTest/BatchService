package com.stockbacktest.batchservice.job.transformetfdailyprice;

import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.TransformEtfDailyPrice;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.EtfInfoMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.EtfsDailyPriceInfoMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.ProductsDailyPriceInfoMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.ProductsDistributionInfoMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
@RequiredArgsConstructor
public class TransformEtfDailyPriceWriter implements ItemWriter<TransformEtfDailyPrice> {

  private final EtfInfoMapper etfInfoMapper;
  private final EtfsDailyPriceInfoMapper etfsDailyPriceInfoMapper;
  private final ProductsDailyPriceInfoMapper productsDailyPriceInfoMapper;
  private final ProductsDistributionInfoMapper productsDistributionInfoMapper;

  @Override
  public void write(Chunk<? extends TransformEtfDailyPrice> chunk) throws Exception {
    List<TransformEtfDailyPrice> transformEtfDailyPrices = new ArrayList<>(
        chunk.getItems());

    if (!transformEtfDailyPrices.isEmpty()) {
      try {
        transformEtfDailyPrices.forEach(
            e -> {
              etfInfoMapper.insert(e.getEtfInfo());
              etfsDailyPriceInfoMapper.insert(e.getEtfsDailyPriceInfo());
              productsDailyPriceInfoMapper.insert(e.getProductsDailyPriceInfo());
              productsDistributionInfoMapper.insertList(e.getProductsDistributionInfo());
            }
        );
      } catch (Exception e) {
        log.error("ETF 변환 데이터 저장 중 문제 발생: ", e);
        log.error(e.getMessage());
      }
    }
  }
}
