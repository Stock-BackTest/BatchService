package com.stockbacktest.batchservice.job.transformetfdailyprice;

import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.Dividends;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.EtfInfo;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.EtfsDailyPriceInfo;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.ProductsDailyPriceInfo;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.ProductsDistributionInfo;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.SecuritiesProductsInfo;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.TransformEtfDailyPrice;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.DividendsMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.SecuritiesProductsInfoMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class TransformEtfDailyPriceProcessor implements
    ItemProcessor<EtfDailyPrice, TransformEtfDailyPrice> {

  private final SecuritiesProductsInfoMapper securitiesProductsInfoMapper;
  private final DividendsMapper dividendsMapper;

  @Override
  public TransformEtfDailyPrice process(EtfDailyPrice item) throws Exception {
    SecuritiesProductsInfo securitiesProductsInfo = securitiesProductsInfoMapper
        .findByIsinShrtCd(item.getIsinShortCode())
        .orElseThrow(
            () -> new RuntimeException("Could not found isinShrtCd: " + item.getIsinShortCode()));
    String isinCd = securitiesProductsInfo.getIsinCd();
    return TransformEtfDailyPrice.builder()
        .etfsDailyPriceInfo(toEtfsDailyPriceInfo(isinCd, item))
        .etfInfo(toEtfInfo(isinCd, item))
        .productsDailyPriceInfo(toProductsDailyPriceInfo(isinCd, item))
        .productsDistributionInfo(toProductsDistributionInfos(isinCd, LocalDate.parse(item.getBaseDate())))
        .build();
  }

  private ProductsDailyPriceInfo toProductsDailyPriceInfo(String isinCd, EtfDailyPrice item) {
    return ProductsDailyPriceInfo.builder()
        .isinCd(isinCd)
        .baseDate(LocalDate.parse(item.getBaseDate()))
        .closePrice(Integer.valueOf(item.getClosePrice()))
        .highestPrice(Integer.valueOf(item.getHighPrice()))
        .lowestPrice(Integer.valueOf(item.getLowPrice()))
        .mrktPrice(Integer.valueOf(item.getOpenPrice()))
        .tradingPrice(Long.valueOf(item.getAccumulatedTradeValue()))
        .tradingQuantity(Integer.valueOf(item.getAccumulatedVolume()))
        .mrktTotalAmount(Long.valueOf(item.getMarketCap()))
        .sharesOutstanding(Long.valueOf(item.getListedShares()))
        .createdAt(LocalDateTime.now())
        .build();
  }

  private EtfsDailyPriceInfo toEtfsDailyPriceInfo(String isinCd, EtfDailyPrice item) {
    return EtfsDailyPriceInfo.builder()
        .isinCd(isinCd)
        .baseDate(LocalDate.parse(item.getBaseDate()))
        .nav(Long.valueOf(item.getNav()))
        .totalNetAssets(Long.valueOf(item.getTotalNetAsset()))
        .createdAt(LocalDateTime.now())
        .build();
  }

  private EtfInfo toEtfInfo(String isinCd, EtfDailyPrice item) {
    return EtfInfo.builder()
        .isinCd(isinCd)
        .etfObjIdxName(item.getIndexName())
//        .managementName() TODO: 뭘로 채워야하는지 모르겠음
//        .taxType()
        .build();
  }

  private List<ProductsDistributionInfo> toProductsDistributionInfos(String isinCd, LocalDate baseDate) {
    return dividendsMapper.findByIsinCd(isinCd, baseDate)
        .stream().map(this::dividendsToProductsDistributionInfo)
        .toList();
  }

  private ProductsDistributionInfo dividendsToProductsDistributionInfo(Dividends dividends) {
    return ProductsDistributionInfo.builder()
        .isinCd(dividends.getIsinCd())
        .distributionType(dividends.getDivType())
        .cashPerShare(dividends.getDistPerShare())
        .recordDate(dividends.getBaseDate())
        .payableDate(dividends.getActualPayDate())
        .taxBasis(dividends.getTaxStd())
        .payoutYieldPct(dividends.getEstmStdPrc())
        .createdAt(LocalDateTime.now())
        .build();
  }
}
