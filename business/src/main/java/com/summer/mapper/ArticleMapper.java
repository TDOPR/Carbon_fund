package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.summer.model.Article;
import com.summer.model.vo.NewsVO;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    List<NewsVO> randomLimit(int id, int language, int limitSize);

}
