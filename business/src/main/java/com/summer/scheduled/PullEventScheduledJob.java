package com.summer.scheduled;

import com.summer.common.annotation.RedisLock;
import com.summer.common.config.AppParamProperties;
import com.summer.common.util.TRXEnableCondition;
import com.summer.manager.EventManager;
import com.summer.manager.TronEventManager;
import com.summer.service.DonaUsersWalletsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Dominick Li
 * @Description 区块链事务拉取类
 * @CreateTime 2022/11/11 17:48
 **/
@Slf4j
@Component
@EnableScheduling
public class PullEventScheduledJob {

    @Autowired
    private EventManager eventManager;

    @Autowired
    private TronEventManager tronEventManager;

    @Autowired
    private AppParamProperties appParamProperties;
    
    @Autowired
    private DonaUsersWalletsService donaUsersWalletsService;

    /**
     * 区块链 每隔3秒拉取事务
     */
    @Scheduled(fixedDelay = 3000)
    @RedisLock
    public void analyzeETHEvent() throws Exception{
        if (appParamProperties.isEnableQueryOrdersStatus()) {
            eventManager.analyzeETHEvent();
        }

    }

//    @Scheduled(fixedDelay = 3000)
    @RedisLock
    public void analyzeBSCEvent() throws Exception{
        if (appParamProperties.isEnableQueryOrdersStatus()) {
            eventManager.analyzeBSCEvent();
        }

    }

//    @Scheduled(fixedDelay = 1000)
    @RedisLock
    @Conditional(TRXEnableCondition.class)
    public void analyzeTRONEvent() throws Exception{
        if (appParamProperties.isEnableQueryOrdersStatus()) {
            tronEventManager.analyzeTRONEvent();
        }

    }
    
//    @Scheduled(cron = "0 0 2 * *
    @Scheduled(cron = "* */10 * * * ?")
    public void deleteTodayTask(){
        log.info("定时任务开始......");
        if (appParamProperties.isEnableQueryOrdersStatus()) {
            donaUsersWalletsService.clearTodayTask();
        }
    }
    
//    @Scheduled(cron = "0 0 23 L * ?")
//    @Scheduled(cron = "*/5 * * * * ?")
    public void sendSuperNodeReward(){
        log.info("开始发放超级节点奖励......");
        if (appParamProperties.isEnableQueryOrdersStatus()) {
            donaUsersWalletsService.superReward();
        }
    }
}