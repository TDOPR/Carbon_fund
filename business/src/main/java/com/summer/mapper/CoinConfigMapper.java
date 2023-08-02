package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summer.model.CoinConfig;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;

public interface CoinConfigMapper extends BaseMapper<CoinConfig> {
    CoinConfig getScanDataConfig(@Param("chainId") Integer chainId);
//    CoinConfig getBscScanDataConfig(@Param("chainId") Integer chainId);
    CoinConfig getDepositScanDataConfig(@Param("chainId") Integer chainId);
//    CoinConfig getBscDepositScanDataConfig(@Param("chainId") Integer chainId);
    int updateActionSeqById(@Param("id") Integer id, @Param("blockNo") BigInteger blockNo);
}
