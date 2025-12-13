package com.stockbacktest.batchservice.scheduler;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EtfDailyPriceScheduler {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
  private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

  private final JobLauncher jobLauncher;
  private final Job importEtfDailyPriceJob;

  /**
   * 매일 오전 9시에 ETF 일일 시세 데이터 수집 배치 실행 전날 데이터를 수집 (ETF 데이터는 장 마감 후 확정되므로)
   */
  @Scheduled(cron = "0 0 9 * * *", zone = "Asia/Seoul")
  public void runEtfDailyPriceJob() {
    LocalDate targetDate = LocalDate.now(ZONE_ID).minusDays(1);  // 전날 데이터

    try {
      log.info("ETF 일일 시세 배치 시작 - 대상 날짜: {} (전날)", targetDate);

      JobParameters jobParameters = new JobParametersBuilder()
          .addString("targetDate", targetDate.format(DATE_FORMATTER))
          .addLong("timestamp", System.currentTimeMillis())
          .toJobParameters();

      jobLauncher.run(importEtfDailyPriceJob, jobParameters);

      log.info("ETF 일일 시세 배치 완료 - 대상 날짜: {}", targetDate);
    } catch (Exception e) {
      log.error("ETF 일일 시세 배치 실패 - 대상 날짜: {}", targetDate, e);
      // TODO: 알림 전송 등 추가 처리
    }
  }
}
