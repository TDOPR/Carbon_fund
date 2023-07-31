package com.summer.common.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/12/19 10:40
 **/
@Data
public class DefaultCondition extends BaseCondition {

    @Override
    public QueryWrapper buildQueryParam() {
        super.buildBaseQueryWrapper();
        return super.getQueryWrapper();
    }

    @Override
    public QueryWrapper buildQueryParam(String orderBy) {
        super.setOderBy(orderBy);
        super.buildBaseQueryWrapper();
        return super.getQueryWrapper();
    }

}
