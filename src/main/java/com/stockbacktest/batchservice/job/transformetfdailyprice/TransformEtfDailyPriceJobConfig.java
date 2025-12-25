package com.stockbacktest.batchservice.job.transformetfdailyprice;

import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.mapper.EtfDailyPriceMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.TransformEtfDailyPrice;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.DividendsMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.EtfInfoMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.EtfsDailyPriceInfoMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.ProductsDailyPriceInfoMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.ProductsDistributionInfoMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.SecuritiesProductsInfoMapper;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransformEtfDailyPriceJobConfig {

  @Bean
  public TransformEtfDailyPriceReader transformEtfDailyPriceReader(
      EtfDailyPriceMapper etfDailyPriceMapper) {
    return new TransformEtfDailyPriceReader(etfDailyPriceMapper);
  }

  @Bean
  public TransformEtfDailyPriceProcessor transformEtfDailyPriceProcessor(
      DividendsMapper dividendsMapper, SecuritiesProductsInfoMapper securitiesProductsInfoMapper) {
    return new TransformEtfDailyPriceProcessor(securitiesProductsInfoMapper, dividendsMapper);
  }

  @Bean
  public TransformEtfDailyPriceWriter transformEtfDailyPriceWriter(EtfInfoMapper etfInfoMapper,
      EtfsDailyPriceInfoMapper etfsDailyPriceInfoMapper,
      ProductsDailyPriceInfoMapper productsDailyPriceInfoMapper,
      ProductsDistributionInfoMapper productsDistributionInfoMapper) {
    return new TransformEtfDailyPriceWriter(etfInfoMapper, etfsDailyPriceInfoMapper,
        productsDailyPriceInfoMapper, productsDistributionInfoMapper);
  }

  @Bean
  public Step trasformEtfDailyPriceStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      TransformEtfDailyPriceReader etfDailyPriceReader,
      TransformEtfDailyPriceProcessor etfDailyPriceProcessor,
      TransformEtfDailyPriceWriter etfDailyPriceWriter
  ) {
    return new StepBuilder(
        "transformEtfDailyPriceStep", jobRepository)
        .<EtfDailyPrice, TransformEtfDailyPrice>chunk(100, transactionManager)
        .reader(etfDailyPriceReader)
        .processor(etfDailyPriceProcessor)
        .writer(etfDailyPriceWriter)
        .build();
  }
}
