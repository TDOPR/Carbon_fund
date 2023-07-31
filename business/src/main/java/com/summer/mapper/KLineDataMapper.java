package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summer.model.KLineData;

import java.math.BigDecimal;

public interface KLineDataMapper extends BaseMapper<KLineData> {
    BigDecimal getNowExchangeRate();
}
