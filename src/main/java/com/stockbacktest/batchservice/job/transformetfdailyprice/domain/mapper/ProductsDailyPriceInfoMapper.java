package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper;

import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.ProductsDailyPriceInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductsDailyPriceInfoMapper {

  void insert(ProductsDailyPriceInfo productsDailyPriceInfo);
}
