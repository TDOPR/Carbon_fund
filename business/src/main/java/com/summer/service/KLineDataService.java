package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.model.KLineData;
import com.summer.model.vo.KLineDataInfoVO;
import com.summer.model.vo.KLineNowDataVO;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface KLineDataService extends IService<KLineData> {

    void insertNow(LocalDate localDate);

    KLineNowDataVO getKLineNowData();

    JsonResult<KLineDataInfoVO> getKLineData();

    BigDecimal getNowExchangeRate();

    JsonResult insertTestData();

    JsonResult addAndEdit(KLineData kLineData);
}
