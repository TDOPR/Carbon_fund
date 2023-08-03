package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summer.model.AppDonaUsers;
import com.summer.model.dto.AllDonaUsersDTO;
import com.summer.model.dto.AllIntegralUsersDTO;
import com.summer.model.dto.HomeUserInfoDTO;
import com.summer.model.vo.MyDirectPushVO;
import com.summer.model.vo.MyMemberInfoVO;
import com.summer.model.vo.MyTeamInfoVO;

import java.math.BigDecimal;
import java.util.List;

public interface AppDonaUsersMapper extends BaseMapper<AppDonaUsers> {
//
//    Integer findInviteIdByUserId(Integer userId);
//
//    IPage<AppUsersVO> page(IPage<AppUsers> page, @Param("param") AppUsersCondition searchParam);
//
//    Integer getValidUserCountByInviteId(Integer userId);
//    int insertBatchNoticeByUserId(Integer userId);
//
//    List<AppUserCountDTO> selectUserCountGroupByLevel();
//
//    String getParentEmail(Integer userId);
//
//    List<String> getChildList(Integer userId);
//
//    List<AppUserCountDTO> selectUserCountGroupByLevelParentId(Integer userId);
    List<AppDonaUsers> findAllUsers();
    
    BigDecimal allUsersRechargeAmount();
    
    Page<AllDonaUsersDTO> allDonaUsers(Page page);
    
    List<AllDonaUsersDTO> selectUsers(String nickName);
    Page<AllIntegralUsersDTO> integralUsersRanking(Page page);
    List<AllIntegralUsersDTO> integralRankingSelect(String nickName);
    AllIntegralUsersDTO myIntegral(String email);
    Integer selectUserIdByEmail(String email);
    
    AllIntegralUsersDTO myIntegralRank(Integer userId);
    
    HomeUserInfoDTO getUserInfo(Integer userId, String email);
    List<MyMemberInfoVO> getMyTeamInfo(Integer userId);
    Page<MyDirectPushVO> getMyDirectPushVO(Integer userId, Page page);
    
    Integer selectDonaUserIdByUserAddress(String userAddress);
}
