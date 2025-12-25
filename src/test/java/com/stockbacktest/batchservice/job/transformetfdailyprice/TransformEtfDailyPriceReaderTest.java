package com.stockbacktest.batchservice.job.transformetfdailyprice;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.entity.EtfDailyPrice;
import com.stockbacktest.batchservice.job.etfdailypriceapi.domain.mapper.EtfDailyPriceMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransformEtfDailyPriceReaderTest {

  private final EtfDailyPriceMapper etfDailyPriceMapper = mock(EtfDailyPriceMapper.class);

  @Test
  @DisplayName("Given_EntityList_When_FirstRead_Then_ReturnsSequentialItems_And_MapperCalledOnce")
  void Given_EntityList_When_FirstRead_Then_ReturnsSequentialItems_And_MapperCalledOnce() throws Exception {
    // given
    EtfDailyPrice firstEntity = mock(EtfDailyPrice.class);
    EtfDailyPrice secondEntity = mock(EtfDailyPrice.class);
    when(etfDailyPriceMapper.findByBaseDate(any(LocalDate.class)))
        .thenReturn(List.of(firstEntity, secondEntity));

    TransformEtfDailyPriceReader reader = new TransformEtfDailyPriceReader(etfDailyPriceMapper);

    // when
    EtfDailyPrice firstRead = reader.read();
    EtfDailyPrice secondRead = reader.read();
    EtfDailyPrice thirdRead = reader.read();

    // then
    assertSame(firstEntity, firstRead);
    assertSame(secondEntity, secondRead);
    assertNull(thirdRead);
    verify(etfDailyPriceMapper, times(1)).findByBaseDate(any(LocalDate.class));
  }

  @Test
  @DisplayName("Given_EmptyResult_When_Read_Then_ReturnsNull")
  void Given_EmptyResult_When_Read_Then_ReturnsNull() throws Exception {
    // given
    when(etfDailyPriceMapper.findByBaseDate(any(LocalDate.class)))
        .thenReturn(Collections.emptyList());

    TransformEtfDailyPriceReader reader = new TransformEtfDailyPriceReader(etfDailyPriceMapper);

    // when
    EtfDailyPrice first = reader.read();
    EtfDailyPrice second = reader.read();

    // then
    assertNull(first);
    assertNull(second);
    verify(etfDailyPriceMapper, times(1)).findByBaseDate(any(LocalDate.class));
  }
}