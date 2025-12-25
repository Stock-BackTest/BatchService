package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper;

import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.ProductsDistributionInfo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductsDistributionInfoMapper {
  void insertList(List<ProductsDistributionInfo> productsDistributionInfos);
}
