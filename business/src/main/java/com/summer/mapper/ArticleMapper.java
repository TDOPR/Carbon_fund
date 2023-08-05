package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summer.model.Article;
import com.summer.model.vo.CarbonInfoVO;
import com.summer.model.vo.NewsVO;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    Page<CarbonInfoVO> carbonInfo(Page page);
    
    CarbonInfoVO carbonInfoDetail(Integer id);

}
