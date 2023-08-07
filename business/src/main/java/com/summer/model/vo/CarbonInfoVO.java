package com.summer.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/27 14:17
 **/
@Data
@Builder
@AllArgsConstructor
public class CarbonInfoVO {


    /**
     * 文章id
     */
    private Integer id;

    /**
     * titile
     */
    private String title;

    /**
     * banner
     */
    private String bannerUrl;
    

    /**
     * 发布时间
     */
    private LocalDateTime createTime;

}
