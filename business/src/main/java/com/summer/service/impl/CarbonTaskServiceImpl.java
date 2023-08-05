package com.summer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.common.config.AppParamProperties;
import com.summer.common.config.GlobalProperties;
import com.summer.common.enums.DataSavePathEnum;
import com.summer.common.model.JsonResult;
import com.summer.common.util.IdUtil;
import com.summer.common.util.StringUtil;
import com.summer.common.util.ThumbnailsUtil;
import com.summer.enums.VipLevelEnum;
import com.summer.mapper.CarbonTaskMapper;
import com.summer.mapper.CertificateMapper;
import com.summer.model.CarbonTask;
import com.summer.model.Certificate;
import com.summer.service.CarbonTaskService;
import com.summer.service.CertificateService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/5/16 11:23
 **/
@Service
public class CarbonTaskServiceImpl extends ServiceImpl<CarbonTaskMapper, CarbonTask> implements CarbonTaskService {


}
