package com.summer.model.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/18 10:16
 **/
@Data
public class SelectMyIntegralDTO {

    /**
     * 用户昵称
     */
    private String nickName;
    
    /**
     * 当前页
     */
    private Integer currentPage = 1;
    
    /**
     * 每页显示的数据
     */
    private Integer pageSize = 10;
    
    /**
     * 是否第一次加载
     */
    private boolean init = true;
    
    public Page getPage() {
        return new Page(currentPage, pageSize);
    }

}
