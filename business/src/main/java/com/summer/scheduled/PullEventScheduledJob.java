//package com.summer.scheduled;
//
//import com.pp.condition.TRXEnableCondition;
//import com.pp.manager.EventManager;
//import com.pp.manager.TronEventManager;
//import com.pp.utils.AppParamProperties;
//import com.pp.utils.annotation.RedisLock;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Conditional;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
///**
// * @author Dominick Li
// * @Description 区块链事务拉取类
// * @CreateTime 2022/11/11 17:48
// **/
//@Slf4j
//@Component
//@EnableScheduling
//public class PullEventScheduledJob {
//
//    @Autowired
//    private EventManager eventManager;
//
//    @Autowired
//    private TronEventManager tronEventManager;
//
//    @Autowired
//    private AppParamProperties appParamProperties;
//
//    /**
//     * 区块链 每隔3秒拉取事务
//     */
//    @Scheduled(fixedDelay = 6000)
//    @RedisLock
//    public void analyzeETHEvent() throws Exception{
//        if (appParamProperties.isEnableQueryOrdersStatus()) {
//            eventManager.analyzeETHEvent();
//        }
//
//    }
//
//    @Scheduled(fixedDelay = 3000)
//    @RedisLock
//    public void analyzeBSCEvent() throws Exception{
//        if (appParamProperties.isEnableQueryOrdersStatus()) {
//            eventManager.analyzeBSCEvent();
//        }
//
//    }
//
//    @Scheduled(fixedDelay = 1000)
//    @RedisLock
//    @Conditional(TRXEnableCondition.class)
//    public void analyzeTRONEvent() throws Exception{
//        if (appParamProperties.isEnableQueryOrdersStatus()) {
//            tronEventManager.analyzeTRONEvent();
//        }
//
//    }
//}
