package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.summer.model.condition.EvmTokenRechargeCondition;
import com.summer.model.usd.EvmRecharge;
import com.summer.model.vo.EvmRechargeVO;
import org.apache.ibatis.annotations.Param;

public interface EvmRechargeMapper extends BaseMapper<EvmRecharge> {
    IPage<EvmRechargeVO> page(IPage<EvmRecharge> page, @Param("param") EvmTokenRechargeCondition searchParam);
}
