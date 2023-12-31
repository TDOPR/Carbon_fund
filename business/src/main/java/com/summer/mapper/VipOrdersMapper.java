package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summer.model.VipOrders;

import java.math.BigDecimal;

public interface VipOrdersMapper extends BaseMapper<VipOrders> {

    BigDecimal sumAllowanceByUserId(Integer userId);

    VipOrders getMaxLevel(Integer userId);

    BigDecimal sumAllowanceByUserIdAndZeroLevel(Integer userId);

    BigDecimal sumAllowanceByZeroLevel();

    BigDecimal sumAllowanceByZeroLevelByParentId(Integer userId);

    void clearFrozenAmount();

}
