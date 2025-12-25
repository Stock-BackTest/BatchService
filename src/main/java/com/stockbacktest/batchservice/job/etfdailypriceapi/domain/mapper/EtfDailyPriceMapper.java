package com.stockbacktest.batchservice.job.etfdailypriceapi.domain.mapper;

import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EtfDailyPriceMapper {

  void insertDailyPrice(@Param("etfPrices") List<EtfDailyPrice> etfPrices);
  List<EtfDailyPrice> findByBaseDate(LocalDate date);
}
