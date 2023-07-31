package com.summer.service;


import com.summer.common.model.JsonResult;

public interface SysDictionaryService {

    JsonResult modifyBaseDictionary(java.util.Map<String, String> data);

    JsonResult reloadSetting();
}
