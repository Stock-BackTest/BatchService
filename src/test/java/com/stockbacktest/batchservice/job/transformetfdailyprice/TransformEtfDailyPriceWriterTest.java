package com.stockbacktest.batchservice.job.transformetfdailyprice;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.EtfInfo;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.EtfsDailyPriceInfo;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.ProductsDailyPriceInfo;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.ProductsDistributionInfo;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.entity.TransformEtfDailyPrice;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.EtfInfoMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.EtfsDailyPriceInfoMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.ProductsDailyPriceInfoMapper;
import com.stockbacktest.batchservice.job.transformetfdailyprice.domain.mapper.ProductsDistributionInfoMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;

@ExtendWith(MockitoExtension.class)
class TransformEtfDailyPriceWriterTest {

  @Mock
  private EtfInfoMapper etfInfoMapper;

  @Mock
  private EtfsDailyPriceInfoMapper etfsDailyPriceInfoMapper;

  @Mock
  private ProductsDailyPriceInfoMapper productsDailyPriceInfoMapper;

  @Mock
  private ProductsDistributionInfoMapper productsDistributionInfoMapper;

  @Test
  @DisplayName("Given_ValidTransformEtfDailyPriceList_When_Write_Then_CallAllMappers")
  void Given_ValidTransformEtfDailyPriceList_When_Write_Then_CallAllMappers() throws Exception {
    // given
    TransformEtfDailyPriceWriter writer = new TransformEtfDailyPriceWriter(
        etfInfoMapper,
        etfsDailyPriceInfoMapper,
        productsDailyPriceInfoMapper,
        productsDistributionInfoMapper
    );

    TransformEtfDailyPrice item = mock(TransformEtfDailyPrice.class);
    EtfInfo etfInfo = mock(EtfInfo.class);
    EtfsDailyPriceInfo etfsDailyPriceInfo = mock(EtfsDailyPriceInfo.class);
    ProductsDailyPriceInfo productsDailyPriceInfo = mock(ProductsDailyPriceInfo.class);
    ProductsDistributionInfo dist1 = mock(ProductsDistributionInfo.class);
    ProductsDistributionInfo dist2 = mock(ProductsDistributionInfo.class);

    when(item.getEtfInfo()).thenReturn(etfInfo);
    when(item.getEtfsDailyPriceInfo()).thenReturn(etfsDailyPriceInfo);
    when(item.getProductsDailyPriceInfo()).thenReturn(productsDailyPriceInfo);
    when(item.getProductsDistributionInfo()).thenReturn(List.of(dist1, dist2));

    Chunk<TransformEtfDailyPrice> chunk = new Chunk<>();
    chunk.add(item);

    // when
    writer.write(chunk);

    // then
    verify(etfInfoMapper, times(1)).insert(etfInfo);
    verify(etfsDailyPriceInfoMapper, times(1)).insert(etfsDailyPriceInfo);
    verify(productsDailyPriceInfoMapper, times(1)).insert(productsDailyPriceInfo);
    verify(productsDistributionInfoMapper, times(1)).insertList(List.of(dist1, dist2));
  }

  @Test
  @DisplayName("Given_EmptyChunk_When_Write_Then_DoNotCallAnyMapper")
  void Given_EmptyChunk_When_Write_Then_DoNotCallAnyMapper() throws Exception {
    // given
    TransformEtfDailyPriceWriter writer = new TransformEtfDailyPriceWriter(
        etfInfoMapper,
        etfsDailyPriceInfoMapper,
        productsDailyPriceInfoMapper,
        productsDistributionInfoMapper
    );

    Chunk<TransformEtfDailyPrice> emptyChunk = new Chunk<>();

    // when
    writer.write(emptyChunk);

    // then
    verifyNoInteractions(etfInfoMapper,
        etfsDailyPriceInfoMapper,
        productsDailyPriceInfoMapper,
        productsDistributionInfoMapper);
  }
}
