package com.stockbacktest.batchservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record EtfDailyPriceApiResponseDto(
        @JsonProperty("OutBlock_1") List<EtfDailyPriceDto> etfDailyPriceList
) { }