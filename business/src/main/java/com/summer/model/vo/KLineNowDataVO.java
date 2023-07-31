package com.summer.model.vo;

import com.summer.model.KLineData;
import lombok.Data;

/**
 * @author Dominick Li
 * @Description
 * @CreateTime 2023/3/2 14:12
 **/
@Data
public class KLineNowDataVO extends KLineDataVO {

    /**
     * 涨跌幅
     */
    private String upDownRange;

    public KLineNowDataVO(KLineData kLineData, String upDownRange) {
        super(kLineData);
        this.upDownRange = upDownRange;
    }

}
