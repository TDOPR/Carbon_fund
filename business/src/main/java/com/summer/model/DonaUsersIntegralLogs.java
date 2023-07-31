package com.summer.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.summer.common.base.BaseModelNoModifyTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description 用户积分流水表
 * @CreateTime 2022/11/1 10:57
 **/
@Data
@Builder
@TableName("dona_users_integral_wallets_logs")
@NoArgsConstructor
@AllArgsConstructor
public class DonaUsersIntegralLogs extends BaseModelNoModifyTime {

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 本次变动积分
     */
    private Integer integralAmount;

    /**
     * 收支类型 1=收入 2=支出
     */
    private Integer action;

    /**
     * 流水类型 对应FlowingTypeEnum枚举
     */
    private Integer type;

    /**
     * 提现状态 0成功 1=审核中 2=驳回 3=驳回对应的单
     */
    private Integer status;

//    /**
//     * 入金和出金渠道
//     */
//    private Integer coinId;
}
