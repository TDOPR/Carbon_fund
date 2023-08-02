package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.model.EvmEvent;

public interface EvmEventService extends IService<EvmEvent> {
    boolean getEventExistByTxHash(String txHash);
}
