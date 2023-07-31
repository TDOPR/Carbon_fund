package com.summer.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.summer.common.base.BaseModelCID;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Dominick Li
 * @Description 业务用户表  客户
 * @CreateTime 2022/11/1 10:30
 **/
@Data
@TableName("app_dona_users")
public class AppDonaUsers extends BaseModelCID {

    /**
     * 邮箱账号
     */
    @NotEmpty
    private String email;

    /**
     * 密码
     */
    @NotEmpty
    private String password;
    
    /**
     * 加密用得盐
     *
     * @ignore
     */
    private String salt;
    

    /**
     * 用户名
     */
    private String nickName;

    /**
     * 登录次数
     */
    private Integer loginCount;

    /**
     * 用户等级
     */
    private Integer level;
    
    /**
     * 超级节点
     */
    private Integer superNode;
    
    /**
     * 已购买的level
     */
    private String buyLevel;
    
    /**
     * 已参与的活动
     */
    private String donaProject;
    
    /**
     * 零级证书
     */
    private String zeroCertificate;
    
    /**
     * 一级证书
     */
    private String oneCertificate;
    
    /**
     * 二级证书
     */
    private String twoCertificate;
    
    /**
     * 三级证书
     */
    private String threeCertificate;
    
    

}
