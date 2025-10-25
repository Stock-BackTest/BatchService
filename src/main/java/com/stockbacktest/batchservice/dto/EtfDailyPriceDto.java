package com.stockbacktest.batchservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record EtfDailyPriceDto(
        @JsonProperty("BAS_DD") String baseDate, // 기준일자
        @JsonProperty("ISU_CD")  String isinCode, // 종목코드
        @JsonProperty("ISU_NM") String itemsName, // 종목명
        @JsonProperty("TDD_CLSPRC") String closePrice, // 종가
        @JsonProperty("CMPPREVDD_PRC") String diffFromPrevPrice, // 대비
        @JsonProperty("FLUC_RT") String fluctuationRate, // 등락률
        @JsonProperty("NAV") String nav, // 순자산가치(NAV)
        @JsonProperty("TDD_OPNPRC") String openPrice, // 시가
        @JsonProperty("TDD_HGPRC") String highPrice, // 고가
        @JsonProperty("TDD_LWPRC") String lowPrice, // 저가
        @JsonProperty("ACC_TRDVOL") String accumulatedVolume, // 거래량
        @JsonProperty("ACC_TRDVAL") String accumulatedTradeValue, // 거래대금
        @JsonProperty("MKTCAP") String marketCap, // 시가총액
        @JsonProperty("INVSTASST_NETASST_TOTAMT") String totalNetAsset, // 순자산총액
        @JsonProperty("LIST_SHRS") String listedShares, // 상장좌수
        @JsonProperty("IDX_IND_NM") String indexName, // 기초지수 지수명
        @JsonProperty("OBJ_STKPRC_IDX") String indexClose, // 기초지수 종가
        @JsonProperty("CMPPREVDD_IDX") String indexDiffFromPrev, // 기초지수_대비
        @JsonProperty("FLUC_RT_IDX") String indexFluctuationRate // 기초지수_등락률
) { }