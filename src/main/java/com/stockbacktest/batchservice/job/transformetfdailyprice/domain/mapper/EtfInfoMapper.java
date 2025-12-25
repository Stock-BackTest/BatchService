package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper;

import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.EtfInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EtfInfoMapper {

  void insert(EtfInfo etfInfo);
}
