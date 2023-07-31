package com.summer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.summer.common.model.JsonResult;
import com.summer.model.AppDonaUserTask;
import com.summer.model.UpdatePwdLog;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/5/16 11:23
 **/
public interface AppDonaUsersDoTaskService extends IService<AppDonaUserTask>  {

//    JsonResult checkUpdateTime();
    JsonResult uploadHeadImage(MultipartFile file, Long id);
//    JsonResult submit(Long id);
    void checkTaskList();

}
