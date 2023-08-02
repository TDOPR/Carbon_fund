package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summer.model.DonaUsersWalletsLogs;
import com.summer.model.Medal;
import com.summer.model.dto.MedalDTO;
import com.summer.model.dto.WalletRecordInfoDTO;
import com.summer.model.vo.RechargeRecordInfoVO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/5 18:06
 **/
public interface DonaUsersWalletsLogsMapper extends BaseMapper<DonaUsersWalletsLogs> {

//    BigDecimal sumLockTal();
//    DateSection getDateSection(Integer userId);
//
//    BigDecimal sumAmountByAndUserIdAndTypeInAndDateRange(@Param("userId") Integer userId,@Param("typeList") List<Integer> depositTypeList, @Param("beginDate") LocalDate beginDate,@Param("endDate") LocalDate endDate);
//
//    List<DonaUsersWalletsLogs> sumAmountGroupByType();
//
//    List<DonaUsersWalletsLogs> sumYesterdayAmountGroupByType();
//
//    BigDecimal sumYesterdayTotalAmountByType(Integer type);
//
//    BigDecimal sumAmountByType(Integer type);
//
////    Page<WalletUsdLogVO> mypage(Page<WalletUsdLogs> page, Integer userId, Integer type, LocalDate beginDate, LocalDate endDate);
//
//    BigDecimal sumToDayTotalAmountByType(Integer value);
//
//    List<WalletUsdLogs> sumAmountGroupByTypeAndParentId(Integer userId);
//
//    List<WalletUsdLogs> sumYesterdayAmountGroupByTypeAndParentId(Integer userId);
    List<MedalDTO> donaUsersLevel(Integer userId);
    
    Page<WalletRecordInfoDTO> selectWalletsLogsRecord(@Param("typeList")List<Integer> typeList, @Param("userId") Integer userId, Page page);
    
    List<MedalDTO> buyLevel(@Param("userId") Integer userId);
    
    List<MedalDTO> medalInfo();
    List<MedalDTO> donaUsersMedalDTO(@Param("userId") Integer userId);
    List<RechargeRecordInfoVO> rechargeRecord(Integer userId);
}
