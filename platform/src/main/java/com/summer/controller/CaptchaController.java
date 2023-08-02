package com.summer.controller;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.convert.Convert;
import com.summer.common.annotation.IgnoreWebSecurity;
import com.summer.common.annotation.PrintLog;
import com.summer.common.config.GlobalProperties;
import com.summer.common.config.LoginConfig;
import com.summer.common.constant.CacheKeyPrefixConstants;
import com.summer.common.enums.CaptchaCategory;
import com.summer.common.enums.CaptchaType;
import com.summer.common.enums.ReturnMessageEnum;
import com.summer.common.model.JsonResult;
import com.summer.common.model.vo.CaptchaVO;
import com.summer.common.util.*;
import com.summer.common.util.redis.RedisUtil;
import com.summer.model.dto.BindInviteCodeDTO;
import com.summer.model.dto.EmailTemplateDTO;
import com.summer.model.dto.SendCaptchaDTO;
import com.summer.model.vo.UuidVO;
import com.summer.server.EmailServer;
import com.summer.service.AppUserService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.Duration;

/**
 * 验证码
 */
@Slf4j
@RestController
public class CaptchaController {

    @Autowired
    private LoginConfig loginConfig;

    @Resource(name = "registerLogin")
    private EmailTemplateDTO emailTemplateDTO;

    @Autowired
    private AppUserService appUserService;

    /**
     * 发送邮件验证码
     *
     * @return 验证码id
     */
    @PrintLog
    @PostMapping("/sendCaptchaToEmail")
    @IgnoreWebSecurity("/sendCaptchaToEmail/**")
    public JsonResult<UuidVO> sendCaptchaToEmail(@Valid @RequestBody SendCaptchaDTO sendCaptchaDTO) {
        int start = NumberUtil.getTenNResult(loginConfig.getCaptcha().getNumberLength());
        int end = start * 10 - 1;
        int intCode = RandomUtil.randomInt(start, end);
        String code=String.valueOf(intCode);
        if (GlobalProperties.isProdEnv()) {
            JsonResult jsonResult = appUserService.isRegisterCheck(sendCaptchaDTO.getEmail(), sendCaptchaDTO.getType());
            if (jsonResult.getCode() != HttpStatus.OK.value()) {
                return jsonResult;
            }
            boolean flag = EmailServer.send(emailTemplateDTO.getTitle(), emailTemplateDTO.getContent().replace("{{code}}", code).replace("{{time}}", loginConfig.getCaptcha().getExpirationTime().toString()), sendCaptchaDTO.getEmail());
            if (flag) {
                String uuid = IdUtil.simpleUUID();
                String verifyKey = CacheKeyPrefixConstants.CAPTCHA_CODE + uuid;
                RedisUtil.setCacheObject(verifyKey, code, Duration.ofMinutes(loginConfig.getCaptcha().getExpirationTime()));
                return JsonResult.successResult("ok", new UuidVO(uuid));
            }
            return JsonResult.failureResult(ReturnMessageEnum.SEND_EMAIL_ERROR);
        }
        String uuid = IdUtil.simpleUUID();
        String verifyKey = CacheKeyPrefixConstants.CAPTCHA_CODE + uuid;
        RedisUtil.setCacheObject(verifyKey, code, Duration.ofMinutes(loginConfig.getCaptcha().getExpirationTime()));
        return JsonResult.successResult("ok", new UuidVO(uuid));
    }

    /**
     * 获取后台登录的验证码
     */
    @GetMapping("/captchaImage")
    @IgnoreWebSecurity
    public JsonResult<CaptchaVO> getCode() {
        // 保存验证码信息
        String uuid = cn.hutool.core.util.IdUtil.simpleUUID();
        String verifyKey = CacheKeyPrefixConstants.CAPTCHA_CODE + uuid;
        // 获取验证码类型
        CaptchaType captchaType = loginConfig.getCaptcha().getType();

        if (captchaType == CaptchaType.RANDOM) {
            //随机策略
            int num = (int) (Math.random() * 2);
            captchaType = num == 0 ? CaptchaType.MATH : CaptchaType.CHAR;
        }

        //获取配置的长度
        boolean isMath = CaptchaType.MATH == captchaType;
        Integer length = isMath ? loginConfig.getCaptcha().getNumberLength() : loginConfig.getCaptcha().getCharLength();

        CodeGenerator codeGenerator = ReflectUtil.newInstance(captchaType.getClazz(), length);

        //获取干扰策略
        CaptchaCategory captchaCategory = loginConfig.getCaptcha().getCategory();

        if (captchaCategory == CaptchaCategory.RANDOM) {
            //随机策略
            int num = (int) (Math.random() * 3);
            if (num == 0) {
                captchaCategory = CaptchaCategory.LINE;
            } else if (num == 1) {
                captchaCategory = CaptchaCategory.CIRCLE;
            } else {
                captchaCategory = CaptchaCategory.SHEAR;
            }
        }

        AbstractCaptcha captcha = SpringUtil.getBean(captchaCategory.getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        String code = isMath ? getCodeResult(captcha.getCode()) : captcha.getCode();
        RedisUtil.setCacheObject(verifyKey, code, Duration.ofMinutes(loginConfig.getCaptcha().getExpirationTime()));
        return JsonResult.successResult(new CaptchaVO(uuid, captcha.getImageBase64()));
    }

    /**
     * 验证码结果
     */
    private String getCodeResult(String capStr) {
        int numberLength = loginConfig.getCaptcha().getNumberLength();
        int a = Convert.toInt(StringUtils.substring(capStr, 0, numberLength).trim());
        char operator = capStr.charAt(numberLength);
        int b = Convert.toInt(StringUtils.substring(capStr, numberLength + 1, numberLength + 1 + numberLength).trim());
        switch (operator) {
            case '*':
                return Convert.toStr(a * b);
            case '+':
                return Convert.toStr(a + b);
            case '-':
                return Convert.toStr(a - b);
            default:
                return StringUtils.EMPTY;
        }
    }

}
