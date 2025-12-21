package com.stockbacktest.batchservice.job.etfdailypriceapi;

import com.stockbacktest.batchservice.dto.EtfDailyPriceDto;
import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.mapper.EtfDailyPriceMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.client.RestClient;

@Configuration
public class EtfDailyPriceApiJobConfig {

  // Reader Bean
  @Bean
  @StepScope
  public EtfDailyPriceApiReader etfDailyPriceApiReader(
      RestClient restClient,
      @Value("#{jobParameters['targetDate']}") String targetDate
  ) {
    LocalDate date = LocalDate.parse(targetDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
    return new EtfDailyPriceApiReader(restClient, date);
  }

  // Processor Bean
  @Bean
  public EtfDailyPriceApiProcessor etfDailyPriceApiProcessor() {
    return new EtfDailyPriceApiProcessor();
  }

  // Writer Bean
  @Bean
  public EtfDailyPriceApiWriter etfDailyPriceApiWriter(EtfDailyPriceMapper mapper) {
    return new EtfDailyPriceApiWriter(mapper);
  }

  // Step
  @Bean
  public Step etfDailyPriceApiStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      EtfDailyPriceApiReader etfDailyPriceApiReader,
      EtfDailyPriceApiProcessor etfDailyPriceApiProcessor,
      EtfDailyPriceApiWriter etfDailyPriceApiWriter
  ) {
    return new StepBuilder("etfDailyPriceApiStep", jobRepository)
        .<EtfDailyPriceDto, EtfDailyPrice>chunk(100, transactionManager)
        .reader(etfDailyPriceApiReader)
        .processor(etfDailyPriceApiProcessor)
        .writer(etfDailyPriceApiWriter)
        .build();
  }

  // Job
  @Bean
  public Job importEtfDailyPriceJob(
      JobRepository jobRepository,
      Step etfDailyPriceApiStep
  ) {
    return new JobBuilder("importEtfDailyPriceJob", jobRepository)
        .start(etfDailyPriceApiStep)
        .build();
  }
}
