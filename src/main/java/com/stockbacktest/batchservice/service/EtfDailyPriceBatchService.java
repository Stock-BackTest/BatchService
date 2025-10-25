package com.stockbacktest.batchservice.service;

import org.springframework.stereotype.Service;

/**
 * ETF 일별 시세 정보
 * https://openapi.krx.co.kr/contents/OPP/USES/service/OPPUSES003_S2.cmd?BO_ID=nrEpCLaZpoLCTzPUMxuF
 */
@Service
public class EtfDailyPriceBatchService {

    private static final String API_URL = "https://data-dbg.krx.co.kr/svc/apis/etp/etf_bydd_trd";


}
