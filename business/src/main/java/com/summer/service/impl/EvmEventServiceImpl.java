package com.summer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.mapper.EvmEventMapper;
import com.summer.model.EvmEvent;
import com.summer.service.EvmEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("EvmEventService")
public class EvmEventServiceImpl extends ServiceImpl<EvmEventMapper, EvmEvent> implements EvmEventService {
    @Autowired
    private EvmEventMapper evmEventMapper;

////    @Override
//    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<EvmEventEntity> page = this.page(
//                new Query<EvmEventEntity>().getPage(params),
//                new QueryWrapper<EvmEventEntity>()
//        );
//
//        return new PageUtils(page);
//    }
    
    @Override
    public boolean getEventExistByTxHash(String txHash) {
        int num = evmEventMapper.getCountByTxHash(txHash);
        return num > 0;
    }
}
