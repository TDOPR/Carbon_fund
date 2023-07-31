
package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summer.model.usd.EvmAddressPool;

public interface EvmAddressPoolMapper extends BaseMapper<EvmAddressPool> {

    EvmAddressPool randomGetAddress(String coinType);

    void deleteByAddress(String address);
}
