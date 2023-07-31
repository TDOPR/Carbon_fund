package com.summer.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dominick Li
 * @Description 提现的货币类型  网络来源
 * @CreateTime 2022/11/1 16:01
 **/
@Getter
@AllArgsConstructor
public enum CoinUnitEnum {

    USDT_ETH(0, "USDTETH", "以太坊USDT"),
    USDT_BSC(1, "USDTBSC", "币安USDT"),
    USDT_TRX(2, "USDTTRX", "波场USDT");

    /**
     * 链ID
     */
    private Integer Id;

    /**
     * 货币名称
     */
    private String name;

    private String cnName;


    public static CoinUnitEnum idOf(Integer id) {
        for (CoinUnitEnum coinUnitEnum : values()) {
            if (coinUnitEnum.getId().equals(id)) {
                return coinUnitEnum;
            }
        }
        return null;
    }

    public static String getNameById(Integer id) {
        for (CoinUnitEnum coinUnitEnum : values()) {
            if (coinUnitEnum.getId().equals(id)) {
                return coinUnitEnum.getName();
            }
        }
        return "";
    }

    public static String getCnNameById(Integer id) {
        for (CoinUnitEnum coinUnitEnum : values()) {
            if (coinUnitEnum.getId().equals(id)) {
                return coinUnitEnum.getCnName();
            }
        }
        return "";
    }

//    public static List<PortraitSelectVO> getSelectList() {
//        List<PortraitSelectVO> list = new ArrayList<>();
//        for (CoinUnitEnum coinUnitEnum : values()) {
//            list.add(new PortraitSelectVO(coinUnitEnum.getCnName(), coinUnitEnum.getId().toString()));
//        }
//        return list;
//    }


}
