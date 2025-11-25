package com.stockbacktest.batchservice.job.etfdailypriceapi;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.mapper.EtfDailyPriceMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;

@ExtendWith(MockitoExtension.class)
class EtfDailyPriceApiWriterTest {

  @Mock
  private EtfDailyPriceMapper mapper;

  @InjectMocks
  private EtfDailyPriceApiWriter writer;

  @Test
  @DisplayName("유효한 Chunk를 넘기면 Mapper가 정상 호출된다")
  void Given_Valid_Chunk_When_Write_Then_Mapper_Is_Called() {
    // Given
    List<EtfDailyPrice> items = List.of(
        createEtfDailyPrice("20251030", "069500", "KODEX 200"),
        createEtfDailyPrice("20251030", "069660", "KODEX 레버리지")
    );
    Chunk<EtfDailyPrice> chunk = new Chunk<>(items);

    // When
    writer.write(chunk);

    // Then
    verify(mapper, times(1)).insertDailyPrice(anyList());
    verify(mapper).insertDailyPrice(argThat(list ->
        list.size() == 2 &&
            list.get(0).getIsinShortCode().equals("069500") &&
            list.get(1).getIsinShortCode().equals("069660")
    ));
  }

  @Test
  @DisplayName("빈 Chunk를 넘기면 Mapper가 호출되지 않는다")
  void Given_Empty_Chunk_When_Write_Then_Mapper_Is_Not_Called() {
    // Given
    Chunk<EtfDailyPrice> chunk = new Chunk<>(List.of());

    // When
    writer.write(chunk);

    // Then
    verify(mapper, never()).insertDailyPrice(anyList());
  }

  @Test
  @DisplayName("NULL인 Chunk를 넘기면 NullPointerException이 throw 된다")
  void Given_Null_Chunk_When_Write_Then_Throw_NullPointerException() {
    // Given & When & Then
    assertThatThrownBy(() -> writer.write(null))
        .isInstanceOf(NullPointerException.class);
  }

  private EtfDailyPrice createEtfDailyPrice(String date, String code, String name) {
    return EtfDailyPrice.builder()
        .baseDate(date)
        .isinShortCode(code)
        .itemsName(name)
        .closePrice("35000")
        .diffFromPrevPrice("100")
        .fluctuationRate("0.29")
        .nav("34500.0")
        .openPrice("34900")
        .highPrice("35100")
        .lowPrice("34800")
        .accumulatedVolume("1000000")
        .accumulatedTradeValue("35000000000")
        .marketCap("1500000000000")
        .totalNetAsset("1480000000000")
        .listedShares("42000000")
        .indexName("KOSPI 200")
        .indexClose("350.5")
        .indexDiffFromPrev("1.2")
        .indexFluctuationRate("0.34")
        .build();
  }
}