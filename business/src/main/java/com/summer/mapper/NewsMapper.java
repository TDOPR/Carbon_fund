package com.summer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.summer.model.News;
import com.summer.model.condition.NewsCondition;
import com.summer.model.dto.NewsDetailsDTO;
import com.summer.model.vo.HomeNoticeVO;
import com.summer.model.vo.NewsInfoVO;
import com.summer.model.vo.NewsVO;
import com.summer.model.vo.SysNoticeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NewsMapper extends BaseMapper<News> {

    Page<News> page(Page page, @Param("language") String language, @Param("param") NewsCondition condition);

    Page<News> clientPage(Page page, @Param("language") String language);

    int insertNewsByLanguage(@Param("id") Integer id, @Param("news") NewsDetailsDTO detail);

    NewsInfoVO getInfoById(@Param("id") Integer id, @Param("language") String language);

    List<NewsVO> randomLimit(@Param("id") Integer id, @Param("language") String language, @Param("limitSize") Integer limitSize);

    int removeByLanguageAndIdIn(@Param("idList") List<Integer> idList, @Param("language") String language);

    int updateNewsByLanguage(@Param("id") Integer id, @Param("news") NewsDetailsDTO detai);

    int insertBatchNoticeUser(Integer noticeId);

    Page<SysNoticeVO> findMyNoticeList(Page page, @Param("userId")Integer userId, @Param("language") String language);

    NewsDetailsDTO getById(Integer id, String language);

    HomeNoticeVO selectForceNotice(String language);
}
