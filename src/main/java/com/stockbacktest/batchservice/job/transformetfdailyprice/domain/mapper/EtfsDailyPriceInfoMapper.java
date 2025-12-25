package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper;

import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.EtfsDailyPriceInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EtfsDailyPriceInfoMapper {

  void insert(EtfsDailyPriceInfo etfsDailyPriceInfo);
}
