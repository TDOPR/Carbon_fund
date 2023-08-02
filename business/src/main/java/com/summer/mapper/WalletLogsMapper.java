package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summer.model.WalletLogs;
import com.summer.model.vo.UserRecordVO;
import org.apache.ibatis.annotations.Param;

public interface WalletLogsMapper extends BaseMapper<WalletLogs> {
    
    Page<UserRecordVO> getPrizeRecord(Page page, @Param("userId") Integer userId);

}
