package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summer.model.EvmEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EvmEventMapper extends BaseMapper<EvmEvent> {
    int getCountByTxHash(@Param("txHash") String txHash);
}
