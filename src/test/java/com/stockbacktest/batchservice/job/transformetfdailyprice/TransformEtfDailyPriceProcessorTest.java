package com.stockbacktest.batchservice.job.transformetfdailyprice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.Dividends;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.SecuritiesProductsInfo;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.TransformEtfDailyPrice;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.DividendsMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.SecuritiesProductsInfoMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransformEtfDailyPriceProcessorTest {

  @Mock
  private SecuritiesProductsInfoMapper securitiesProductsInfoMapper;

  @Mock
  private DividendsMapper dividendsMapper;

  @Test
  @DisplayName("Given_ValidEtfDailyPrice_When_Process_Then_ReturnsTransformedEntity")
  void Given_ValidEtfDailyPrice_When_Process_Then_ReturnsTransformedEntity() throws Exception {
    // Given
    String isinShortCode = "KR123456";
    String isinCd = "KR123456789012";
    LocalDate baseDate = LocalDate.of(2025, 12, 25);

    EtfDailyPrice etfDailyPrice = EtfDailyPrice.builder()
        .baseDate(baseDate.toString())
        .isinShortCode(isinShortCode)
        .closePrice("1000")
        .highPrice("1100")
        .lowPrice("900")
        .openPrice("950")
        .accumulatedTradeValue("1000000")
        .accumulatedVolume("5000")
        .marketCap("99999999")
        .listedShares("7777777")
        .nav("12345")
        .totalNetAsset("55555555")
        .indexName("KOSPI200")
        .build();

    SecuritiesProductsInfo securitiesProductsInfo = SecuritiesProductsInfo.builder()
        .isinCd(isinCd)
        .isinShrtCd(isinShortCode)
        .build();

    Dividends dividend = Dividends.builder()
        .isinCd(isinCd)
        .divType("CASH")
        .distPerShare(10.5)
        .baseDate(baseDate)
        .actualPayDate(baseDate.plusDays(7))
        .taxStd(15.0)
        .estmStdPrc(3.2)
        .build();

    when(securitiesProductsInfoMapper.findByIsinShrtCd(isinShortCode))
        .thenReturn(Optional.of(securitiesProductsInfo));
    when(dividendsMapper.findByIsinCd(isinCd, baseDate))
        .thenReturn(List.of(dividend));

    TransformEtfDailyPriceProcessor processor =
        new TransformEtfDailyPriceProcessor(securitiesProductsInfoMapper, dividendsMapper);

    // When
    TransformEtfDailyPrice result = processor.process(etfDailyPrice);

    // Then
    assertNotNull(result);
    assertEquals(isinCd, result.getEtfsDailyPriceInfo().getIsinCd());
    assertEquals(baseDate, result.getEtfsDailyPriceInfo().getBaseDate());
    assertEquals(isinCd, result.getProductsDailyPriceInfo().getIsinCd());
    assertEquals(1000, result.getProductsDailyPriceInfo().getClosePrice());
    assertEquals("KOSPI200", result.getEtfInfo().getEtfObjIdxName());
    assertEquals(1, result.getProductsDistributionInfo().size());
  }

  @Test
  @DisplayName("Given_MissingSecuritiesProductInfo_When_Process_Then_ThrowsException")
  void Given_MissingSecuritiesProductInfo_When_Process_Then_ThrowsException() {
    // Given
    String isinShortCode = "KR123000";
    EtfDailyPrice etfDailyPrice = EtfDailyPrice.builder()
        .isinShortCode(isinShortCode)
        .baseDate("2025-12-25")
        .build();

    when(securitiesProductsInfoMapper.findByIsinShrtCd(isinShortCode))
        .thenReturn(Optional.empty());

    TransformEtfDailyPriceProcessor processor =
        new TransformEtfDailyPriceProcessor(securitiesProductsInfoMapper, dividendsMapper);

    // When + Then
    RuntimeException ex = assertThrows(RuntimeException.class,
        () -> processor.process(etfDailyPrice));
    assertTrue(ex.getMessage().contains(isinShortCode));
  }
}
