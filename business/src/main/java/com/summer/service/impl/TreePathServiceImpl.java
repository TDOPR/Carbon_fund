package com.summer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.model.JsonResult;
import com.summer.constant.TiktokConfig;
import com.summer.enums.TttLogTypeEnum;
import com.summer.mapper.TreePathMapper;
import com.summer.model.TreePath;
import com.summer.model.dto.TreePathAmountDTO;
import com.summer.model.dto.TreePathLevelDTO;
import com.summer.model.dto.TreeUserIdDTO;
import com.summer.model.vo.MyCommunityAdminVO;
import com.summer.model.vo.MyCommunityVO;
import com.summer.service.TreePathService;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.List;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/1 12:20
 **/
@Service
public class TreePathServiceImpl extends ServiceImpl<TreePathMapper, TreePath> implements TreePathService {

    @Override
    public List<TreePath> getThreeAlgebraTreePathByUserId(Integer userId) {
        return this.list(new LambdaQueryWrapper<TreePath>().select(TreePath::getDescendant, TreePath::getLevel).eq(TreePath::getAncestor, userId).in(TreePath::getLevel, TiktokConfig.ALGEBRA_LEVEL));
    }

    @Override
    public List<TreePath> getTreePathByUserId(Integer userId) {
        return this.list(new LambdaQueryWrapper<TreePath>().select(TreePath::getDescendant, TreePath::getLevel).eq(TreePath::getAncestor, userId));
    }

    @Override
    public Integer countByAncestor(Integer userId) {
        return (int) this.count(
                new LambdaQueryWrapper<TreePath>()
                        .eq(TreePath::getAncestor, userId)
                        .gt(TreePath::getLevel, 0)
        );
    }

    @Override
    public void insertTreePath(Integer id, Integer inviteUserId) {
        this.baseMapper.insertTreePath(id, inviteUserId);
    }

    @Override
    public List<TreePathAmountDTO> getTaskEarningsByUserIdAndLevelList(Integer userId, List<Integer> levelList) {
        return this.baseMapper.getTaskEarningsByUserIdAndLevelList(userId, levelList, Arrays.asList(TttLogTypeEnum.LIKE.getValue(), TttLogTypeEnum.CONCERN.getValue(), TttLogTypeEnum.COMMENTS.getValue()));
    }

    @Override
    public JsonResult<List<TreeUserIdDTO>> findTreeById(Integer userId) {
        return JsonResult.successResult(this.baseMapper.findTreeById(userId));
    }

    @Override
    public List<TreePathLevelDTO> getTreePathLevelOrderByLevel(Integer userId) {
        return this.baseMapper.getTreePathLevelOrderByLevel(userId);
    }

    @Override
    public MyCommunityVO getItemInfoByUserId(Integer userId) {
        return MyCommunityVO.builder()
                .validUser(this.baseMapper.teamSumValid(userId))
                .meshUser(this.baseMapper.teamMeshUser(userId))
                .starUser(this.baseMapper.teamStarSum(userId))
                .allUser(this.baseMapper.teamSum(userId))
                .build();
    }

    @Override
    public MyCommunityAdminVO getAdminItemInfoByUserId(Integer userId) {
        Integer allUser = this.baseMapper.teamSum(userId);
        Integer vaildUser = this.baseMapper.teamMeshUser(userId);
        return MyCommunityAdminVO.builder()
                .meshUser(vaildUser)
                .zeroUser(allUser - vaildUser)
                .starUser(this.baseMapper.teamStarSum(userId))
                .allUser(allUser)
                .build();
    }
}
