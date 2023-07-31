package com.summer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.config.AppParamProperties;
import com.summer.common.config.GlobalProperties;
import com.summer.common.enums.DataSavePathEnum;
import com.summer.common.enums.ReturnMessageEnum;
import com.summer.common.model.JsonResult;
import com.summer.common.model.ThreadLocalManager;
import com.summer.common.util.*;
import com.summer.constant.CarbonConfig;
import com.summer.enums.CarbonLogTypeEnum;
import com.summer.enums.VipLevelEnum;
import com.summer.mapper.CertificateMapper;
import com.summer.mapper.UpdatePwdLogMapper;
import com.summer.model.Certificate;
import com.summer.model.UpdatePwdLog;
import com.summer.service.CertificateService;
import com.summer.service.UpdatePwdLogService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/5/16 11:23
 **/
@Service
public class CertificateServiceImpl extends ServiceImpl<CertificateMapper, Certificate> implements CertificateService {

    @Resource
    private AppParamProperties appParamProperties;
    @Resource
    private ThumbnailsService thumbnailsService;
    @Resource
    private MyIncrementGenerator myIncrementGenerator;
    
    @Override
    public JsonResult generateCertificate(Integer userId, String name, String number, String level, LocalDateTime date){
        try{
            Certificate certificate = new Certificate();
            certificate.setUserId(userId);
            certificate.setLevel(Integer.valueOf(level));
            DataSavePathEnum dataSavePathEnum = DataSavePathEnum.CERTIFICATE;
            String saveFileName = IdUtil.simpleUUID() + "." + appParamProperties.getSuffix();
            File saveFile = new File(dataSavePathEnum.getFile(), saveFileName);
            FileInputStream fileInputStream = new FileInputStream(new File(appParamProperties.getSourceFile()));
//            String number = myIncrementGenerator.usingMath(CarbonConfig.STRING_LENGTH);
            InputStream result = ThumbnailsService.addWaterMark(fileInputStream, level, name, number, String.valueOf(date));
            FileUtils.copyInputStreamToFile(result, saveFile);
            String url = GlobalProperties.getVirtualPathURL() + StringUtil.replace(dataSavePathEnum.getPath(), GlobalProperties.getRootPath(), "") + saveFileName;
            certificate.setFolderPath(saveFile.getAbsolutePath());
            certificate.setUrl(url);
            return JsonResult.successResult();
        } catch (Exception e){
            log.error("banner: saveAndUpload error:{}", e);
            return JsonResult.failureResult();
        }
        
    }
}
