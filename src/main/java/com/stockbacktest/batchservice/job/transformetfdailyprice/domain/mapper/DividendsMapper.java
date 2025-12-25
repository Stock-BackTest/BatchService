package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper;

import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.Dividends;
import java.time.LocalDate;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DividendsMapper {

  List<Dividends> findByIsinCd(@Param("isinCd") String isinCd,
      @Param("baseDate") LocalDate baseDate);
}
