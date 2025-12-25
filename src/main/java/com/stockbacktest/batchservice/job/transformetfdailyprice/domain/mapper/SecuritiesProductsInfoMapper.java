package com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper;

import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.SecuritiesProductsInfo;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SecuritiesProductsInfoMapper {
  Optional<SecuritiesProductsInfo> findByIsinShrtCd(@Param("isinShrtCd") String isinShrtCd);
}
