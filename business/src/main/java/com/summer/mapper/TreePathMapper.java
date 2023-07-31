package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summer.model.TreePath;
import com.summer.model.dto.TreePathAmountDTO;
import com.summer.model.dto.TreePathLevelDTO;
import com.summer.model.dto.TreeUserIdDTO;
import org.apache.ibatis.annotations.Param;


import java.math.BigDecimal;
import java.util.List;

/**
 * @author Administrator
 */
public interface TreePathMapper extends BaseMapper<TreePath> {

    int insertTreePath(int uid,@Param("pid") int pid);

    List<TreePathAmountDTO> getTaskEarningsByUserIdAndLevelList(@Param("uid")Integer userId, @Param("levelList") List<Integer> levelList, @Param("typeList") List<Integer> typeList);

    BigDecimal getMyItemAmountByUserId(@Param("uid")Integer userId);

    List<Integer> getAllAncestorIdByUserId(@Param("uid")Integer userId);

    List<TreeUserIdDTO> findTreeById(Integer userId);

    Integer teamSumValid(Integer userId);

    Integer teamStarSum(Integer userId);

    List<TreePathLevelDTO> getTreePathLevelOrderByLevel(@Param("uid")Integer userId);

    Integer getGenerationUserNum(Integer userId);

    Integer getItemUserNum(Integer userId);

    Integer teamSum(Integer userId);

    Integer teamMeshUser(Integer userId);
    
    List<TreePath> getTeam(@Param("uid")Integer userId);
    
    Integer getTeamNum(@Param("uid")Integer userId);
    
    BigDecimal getTeamRechargeAmount(@Param("uid")Integer userId);
}
