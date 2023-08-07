package com.summer.model.condition;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.summer.common.base.BaseCondition;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Dominick Li
 * @Description 审核列表使用的查询条件
 * @CreateTime 2023/3/1 15:53
 **/
@Data
public class GlobalUserCondition extends BaseCondition<LocalDateTime> {

    /**
     * 用户昵称
     */
    private String nickName;


    @Override
    public QueryWrapper buildQueryParam() {
        return null;
    }
}
