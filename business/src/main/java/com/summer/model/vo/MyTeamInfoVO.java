package com.summer.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/16 11:35
 **/
@Data
@Builder
@AllArgsConstructor
public class MyTeamInfoVO {

    /**
     * 我的团队成员信息
     *
     */
    private List<MyMemberInfoVO> memberInfoVOS;

    /**
     * 我的直推信息
     *
     */
    private List<MyDirectPushVO> myDirectPushVOS;

}
