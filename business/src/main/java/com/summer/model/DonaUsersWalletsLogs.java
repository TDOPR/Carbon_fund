package com.summer.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.summer.common.base.BaseModel;
import com.summer.common.base.BaseModelCID;
import com.summer.common.base.BaseModelNoModifyTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Dominick Li
 * @Description 用户钱包流水表
 * @CreateTime 2022/11/1 10:57
 **/
@Data
@Builder
@TableName("dona_users_wallets_logs")
@NoArgsConstructor
@AllArgsConstructor
public class DonaUsersWalletsLogs extends BaseModel {

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 本次变动金额
     */
    private BigDecimal amount;
    
    /**
     * 本次变动积分
     */
    private BigDecimal integralAmount;

    /**
     * 收支类型 1=收入 2=支出
     */
    private Integer action;

    /**
     * 流水类型 对应FlowingTypeEnum枚举
     */
    private Integer type;



    /**
     * 捐赠等级
     */
    private Integer donaLevel;
}
