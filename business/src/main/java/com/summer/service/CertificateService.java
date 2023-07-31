package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.model.Certificate;
import com.summer.model.UpdatePwdLog;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/5/16 11:23
 **/
public interface CertificateService extends IService<Certificate>  {
    
    JsonResult generateCertificate(Integer userId, String name, String number, String Level, LocalDateTime date);

}
