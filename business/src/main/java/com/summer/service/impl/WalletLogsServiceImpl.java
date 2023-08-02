package com.summer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.summer.enums.FlowingActionEnum;
import com.summer.enums.FlowingTypeEnum;
import com.summer.mapper.WalletLogsMapper;
import com.summer.model.WalletLogs;
import com.summer.service.WalletLogsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2022/11/1 18:54
 **/
@Service
public class WalletLogsServiceImpl extends ServiceImpl<WalletLogsMapper, WalletLogs> implements WalletLogsService {
    
    
    /**
     * 动态奖励类型
     */
    private final List<Integer> dynamicTypeList = Arrays.asList(FlowingTypeEnum.ALGEBRA.getValue(), FlowingTypeEnum.ROBOT.getValue(), FlowingTypeEnum.TEAM.getValue(), FlowingTypeEnum.SPECIAL.getValue());
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertWalletAlegraLogs(Integer userId, Integer subId, String subUserAddress, Integer subUserLevel, BigDecimal amount, FlowingActionEnum flowingActionEnum, FlowingTypeEnum flowingTypeEnum, Integer chainId) {
        //添加钱包流水记录
        WalletLogs walletLogs = WalletLogs.builder()
                .userId(userId)
                .subId(subId)
                .subUserAddress(subUserAddress)
                .subUserLevel(subUserLevel)
                .amount(amount)
                .action(flowingActionEnum.getValue())
                .type(flowingTypeEnum.getValue())
                .build();
        return this.baseMapper.insert(walletLogs) > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertWalletLogs(Integer userId, BigDecimal amount, FlowingActionEnum flowingActionEnum, FlowingTypeEnum flowingTypeEnum, Integer chainId) {
        //添加钱包流水记录
        WalletLogs walletLogs = WalletLogs.builder()
                .userId(userId)
                .amount(amount)
                .action(flowingActionEnum.getValue())
                .type(flowingTypeEnum.getValue())
                .build();
        return this.baseMapper.insert(walletLogs) > 0;
    }
    
//    @Override
//    public JsonResult<List<WalletLogVO>> listByUserIdAndType(Integer userId, Integer type) {
//        List<WalletLogs> walletLogsList = this.list(new LambdaQueryWrapper<WalletLogs>()
//                .select(WalletLogs::getCreateTime, WalletLogs::getAmount, WalletLogs::getType, WalletLogs::getAction)
//                .eq(WalletLogs::getUserId, userId)
//                .eq(WalletLogs::getType, type)
//                .orderByDesc(WalletLogs::getCreateTime));
//
//        List<WalletLogVO> walletLogVOList = new ArrayList<>();
//        for (WalletLogs walletLogs : walletLogsList) {
//            walletLogVOList.add(WalletLogVO.builder()
//                    .createTime(walletLogs.getCreateTime().toLocalDate().toString())
//                    .amount(NumberUtil.toMoeny(walletLogs.getAmount()))
//                    .name(FlowingTypeEnum.getDescByValue(walletLogs.getType()))
//                    .build());
//        }
//        return JsonResult.successResult(walletLogVOList);
//    }
//
//    @Override
//    public MyWalletsVO.Proxy getMyProxyWalletLogs(Integer userId) {
//        BigDecimal algebra = BigDecimal.ZERO, rebot = BigDecimal.ZERO, team = BigDecimal.ZERO, special = BigDecimal.ZERO;
//        List<WalletLogs> walletLogsList = this.baseMapper.getMyProxyWalletLogs(userId, dynamicTypeList);
//        for (WalletLogs walletLogs : walletLogsList) {
//            if (walletLogs.getType().equals(FlowingTypeEnum.ALGEBRA.getValue())) {
//                algebra = walletLogs.getAmount();
//            } else if (walletLogs.getType().equals(FlowingTypeEnum.ROBOT.getValue())) {
//                rebot = walletLogs.getAmount();
//            } else if (walletLogs.getType().equals(FlowingTypeEnum.TEAM.getValue())) {
//                team = walletLogs.getAmount();
//            } else {
//                special = walletLogs.getAmount();
//            }
//        }
//        return MyWalletsVO.Proxy.builder()
//                .algebra(NumberUtil.toTwoDecimal(algebra))
//                .rebot(NumberUtil.toTwoDecimal(rebot))
//                .team(NumberUtil.toTwoDecimal(team))
//                .special(NumberUtil.toTwoDecimal(special))
//                .totalAmount(NumberUtil.toTwoDecimal(algebra.add(rebot).add(team).add(special)))
//                .build();
//    }
//
//    @Override
//    public JsonResult<WalletLogsDetailVO> getMybillDetails(BillDetailsDTO billDetailsDTO) {
//        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
//
//        //查询钱包流水中第一笔流水的时间
//        DateSection dateSection = this.baseMapper.getDateSection(userId);
//        //获取日期下拉列表
//        List<ViewSelectVO> dateSelectVOList = billDetailsDTO.isInit() ? getSelectListByUser(dateSection) : null;
//        //根据类型查询
//        List<Integer> typeList = new ArrayList<>();
//
//        //判断是否查询所有还是根据指定月份查询
//        LocalDate beginDate = null, endDate = null;
//        if (!billDetailsDTO.getYearMonth().equals("-1")) {
//            //分割年月
//            String arr[] = billDetailsDTO.getYearMonth().split("-");
//            beginDate = LocalDate.of(Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), 1);
//            endDate = beginDate.plusMonths(1);
//        }
//
//        List<WalletLogVO> resultList = new ArrayList<>();
//        long totalPage;
//
//        Page<WalletLogs> pageData = new Page<>(billDetailsDTO.getCurrentPage(), billDetailsDTO.getPageSize());
//        if (billDetailsDTO.getType() == 0) {
//            //查询代理收益 TODO
//            pageData = this.baseMapper.pageByDynamic(pageData, userId, beginDate, endDate, dynamicTypeList);
//        } else if (billDetailsDTO.getType() == -1) {
//            //查询所有
//            pageData = this.baseMapper.pageByAllType(pageData, userId, beginDate, endDate, dynamicTypeList);
//        } else {
//            //查询其它类型
//            LambdaQueryWrapper<WalletLogs> lambdaQueryWrapper = new LambdaQueryWrapper<WalletLogs>()
//                    .select(WalletLogs::getCreateTime, WalletLogs::getAmount, WalletLogs::getType, WalletLogs::getAction)
//                    .eq(WalletLogs::getUserId, userId)
//                    .eq(WalletLogs::getType, billDetailsDTO.getType())
//                    .orderByDesc(WalletLogs::getCreateTime);
//            pageData = this.page(pageData, lambdaQueryWrapper);
//        }
//        List<WalletLogs> walletLogsList = pageData.getRecords();
//        totalPage = pageData.getPages();
//
//        for (WalletLogs walletLogs : walletLogsList) {
//            resultList.add(WalletLogVO.builder()
//                    .createTime(walletLogs.getCreateTime().toLocalDate().toString())
//                    .amount((walletLogs.getAction() == 1 ? "+ " : "- ") + NumberUtil.toUSD(walletLogs.getAmount()))
//                    .name(FlowingTypeEnum.getWalletDescByValue(walletLogs.getType()))
//                    .build());
//        }
//
//        //计算存入和取出金额
//        List<Integer> logTypeList = new ArrayList<>();
//        logTypeList.add(FlowingTypeEnum.STATIC.getValue());
//        logTypeList.add(FlowingTypeEnum.RECHARGE.getValue());
//        logTypeList.addAll(dynamicTypeList);
//
//        BigDecimal deposit = this.baseMapper.sumAmountByAndUserIdAndTypeListAndDateRange(userId, logTypeList, beginDate, endDate);
//        BigDecimal takeOut = this.baseMapper.sumAmountByAndUserIdAndTypeListAndDateRange(userId, Arrays.asList(FlowingTypeEnum.WITHDRAWAL.getValue()), beginDate, endDate);
//
//        //存入类型
//        List<Integer> depositTypeList = new ArrayList<>();
//        depositTypeList.addAll(Arrays.asList(FlowingTypeEnum.RECHARGE.getValue(), FlowingTypeEnum.STATIC.getValue()));
//        depositTypeList.addAll(dynamicTypeList);
//
//        List<ViewSelectVO> viewTypeList = billDetailsDTO.isInit() ? WalletLogsDetailVO.buildTypeList() : null;
//        return JsonResult.successResult(WalletLogsDetailVO.builder()
//                .deposit(NumberUtil.toMoeny(deposit))
//                .takeOut(NumberUtil.toMoeny(takeOut))
//                .tableList(resultList)
//                .typeList(viewTypeList)
//                .dateSectionList(dateSelectVOList)
//                .totalPage((int) totalPage)
//                .build());
//    }
//
//    /**
//     * 获取钱包流水月份title
//     */
//    private List<ViewSelectVO> getSelectListByUser(DateSection dateSection) {
//        List<ViewSelectVO> viewSelectVOList = new ArrayList<>();
//        viewSelectVOList.add(new ViewSelectVO(MessageUtil.get(LanguageKeyConstants.All, ThreadLocalManager.getLanguage()), "-1"));
//        if (dateSection != null && dateSection.getMaxDate() != null) {
//            viewSelectVOList.add(new ViewSelectVO(MessageUtil.getMonthStrByLanguage(dateSection.getMaxDate().getMonthValue()), dateSection.getMaxDate().getYear() + "-" + dateSection.getMaxDate().getMonthValue()));
//            int monthNumber = DateUtil.betweenMonths(dateSection.getMinDate(), dateSection.getMaxDate());
//            if (monthNumber > 11) {
//                monthNumber = 11;
//            }
//            LocalDate localDate = dateSection.getMaxDate();
//            for (int i = 1; i <= monthNumber; i++) {
//                //生成最近12个月的 年-月数据
//                localDate = localDate.minusMonths(1);
//                viewSelectVOList.add(new ViewSelectVO(MessageUtil.getMonthStrByLanguage(localDate.getMonthValue()), localDate.getYear() + "-" + localDate.getMonthValue()));
//            }
//        }
//        return viewSelectVOList;
//    }
//
//    @Override
//    public JsonResult<ProfitLogsDetailVO> quantificationDetail(TypeDTO typeDTO) {
//        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
//
//        LambdaQueryWrapper<ProfitLogs> lambdaQueryWrapper = new LambdaQueryWrapper<ProfitLogs>()
//                .select(ProfitLogs::getPrincipal, ProfitLogs::getGeneratedAmount, ProfitLogs::getStatus, ProfitLogs::getCreateDate)
//                .orderByDesc(ProfitLogs::getCreateDate)
//                .eq(ProfitLogs::getUserId, userId);
//
//        //是否根据交割类型查询数据
//        if (typeDTO.getType() > -1) {
//            lambdaQueryWrapper.eq(ProfitLogs::getStatus, typeDTO.getType());
//        }
//
//        Page<ProfitLogs> page = profitLogsService.page(new Page<>(typeDTO.getCurrentPage(), typeDTO.getPageSize()), lambdaQueryWrapper);
//        List<ProfitLogsDetailVO.ProfitLogsVO> profitLogsVOList = new ArrayList<>();
//
//        BigDecimal settled = profitLogsService.getTotalAmountByUserIdAndType(userId, 1);
//        BigDecimal noSettled = profitLogsService.getTotalAmountByUserIdAndType(userId, 0);
//        if (settled == null) {
//            settled = BigDecimal.ZERO;
//        }
//
//        if (noSettled == null) {
//            noSettled = BigDecimal.ZERO;
//        }
//        for (ProfitLogs profitLogs : page.getRecords()) {
//            profitLogsVOList.add(ProfitLogsDetailVO.ProfitLogsVO.builder()
//                    .createTime(profitLogs.getCreateDate().toString())
//                    .generatedAmount(NumberUtil.toTwoDecimal(profitLogs.getGeneratedAmount()))
//                    .principal(NumberUtil.toTwoDecimal(profitLogs.getPrincipal()))
//                    .type(profitLogs.getStatus())
//                    .build());
//        }
//
//        List<ViewSelectVO> typeList = null;
//        if (typeDTO.isInit()) {
//            //首页加载需要返回类型列表
//            typeList = new ArrayList<>();
//            typeList.add(new ViewSelectVO(MessageUtil.get(LanguageKeyConstants.All, ThreadLocalManager.getLanguage()), "-1"));
//            typeList.add(new ViewSelectVO(MessageUtil.get(LanguageKeyConstants.QUANTIFICATION_TYPE_ALREADY, ThreadLocalManager.getLanguage()), "1"));
//            typeList.add(new ViewSelectVO(MessageUtil.get(LanguageKeyConstants.QUANTIFICATION_TYPE_NOT, ThreadLocalManager.getLanguage()), "0"));
//        }
//
//        return JsonResult.successResult(ProfitLogsDetailVO.builder()
//                .totalPage((int) page.getPages())
//                .list(profitLogsVOList)
//                .typeList(typeList)
//                .settled(NumberUtil.toTwoDecimal(settled))
//                .noSettled(NumberUtil.toTwoDecimal(noSettled))
//                .totalAmount(NumberUtil.toTwoDecimal(settled.add(noSettled)))
//                .build());
//    }
//
//    @Override
//    public JsonResult<ProxyWalletLogsDetailVO> proxyDetail(TypeDTO typeDTO) {
//        Integer userId = JwtTokenUtil.getUserIdFromToken(ThreadLocalManager.getToken());
//        LambdaQueryWrapper<WalletLogs> lambdaQueryWrapper = new LambdaQueryWrapper<WalletLogs>()
//                .select(WalletLogs::getCreateTime, WalletLogs::getAmount, WalletLogs::getType)
//                .eq(WalletLogs::getUserId, userId)
//                .orderByDesc(WalletLogs::getCreateTime);
//
//        if (typeDTO.getType() == -1) {
//            //查询所有动态奖励
//            lambdaQueryWrapper.in(WalletLogs::getType, dynamicTypeList);
//        } else {
//            //根据类型查询
//            lambdaQueryWrapper.eq(WalletLogs::getType, typeDTO.getType());
//        }
//
//        Page<WalletLogs> page = this.page(new Page<>(typeDTO.getCurrentPage(), typeDTO.getPageSize()), lambdaQueryWrapper);
//        List<WalletLogVO> walletLogVOList = new ArrayList<>();
//
//        String typeName;
//        for (WalletLogs walletLogs : page.getRecords()) {
//            typeName = FlowingTypeEnum.getDescByValue(walletLogs.getType());
//            walletLogVOList.add(WalletLogVO.builder()
//                    .createTime(walletLogs.getCreateTime().toLocalDate().toString())
//                    .amount(NumberUtil.toMoeny(walletLogs.getAmount()))
//                    .name(typeName)
//                    .build());
//        }
//
//        ProxyWalletLogsDetailVO proxyWalletLogsDetailVO = new ProxyWalletLogsDetailVO();
//        proxyWalletLogsDetailVO.setList(walletLogVOList);
//        proxyWalletLogsDetailVO.setTotalPage((int) page.getPages());
//
//        if (typeDTO.isInit()) {
//            //首页加载需要返回类型列表
//            List<ViewSelectVO> typeList = new ArrayList<>();
//            typeList.add(new ViewSelectVO(MessageUtil.get(LanguageKeyConstants.All, ThreadLocalManager.getLanguage()), "-1"));
//            FlowingTypeEnum flowingTypeEnum;
//            for (Integer type : dynamicTypeList) {
//                flowingTypeEnum = FlowingTypeEnum.valueOf(type);
//                typeList.add(new ViewSelectVO(flowingTypeEnum.toString(), flowingTypeEnum.getValue().toString()));
//            }
//            proxyWalletLogsDetailVO.setTypeList(typeList);
//        }
//
//        MyWalletsVO.Proxy proxy = this.getMyProxyWalletLogs(userId);
//        BeanUtils.copyProperties(proxy, proxyWalletLogsDetailVO);
//        return JsonResult.successResult(proxyWalletLogsDetailVO);
//    }
//
//    @Override
//    public BigDecimal sumTotalAmountByType(Integer type) {
//        return this.baseMapper.sumTotalAmountByType(type);
//    }
//
//    @Override
//    public BigDecimal sumProfitTotalAmount() {
//        List<Integer> typeList = new ArrayList<>(dynamicTypeList);
//        typeList.add(FlowingTypeEnum.STATIC.getValue());
//        typeList.add(FlowingTypeEnum.WITHDRAWAL.getValue());
//        return this.baseMapper.sumProfitTotalAmountByTypeIn(typeList);
//    }
//
//    @Override
//    public BigDecimal sumTotalAmountByTypeIn(List<Integer> typeList) {
//        return this.baseMapper.sumProfitTotalAmountByTypeIn(typeList);
//    }
//
//    @Override
//    public BigDecimal getDynamicAmountByUserId(Integer userId) {
//        return this.baseMapper.sumProfitTotalAmountByUserIdAndTypeIn(userId, dynamicTypeList);
//    }
}
