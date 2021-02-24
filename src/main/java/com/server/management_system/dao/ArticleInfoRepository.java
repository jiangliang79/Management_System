package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.ArticleInfo;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-24
 */
@Mapper
public interface ArticleInfoRepository extends BaseMapper<ArticleInfo> {
    @Select("select * from article_info where deleted = 0")
    List<ArticleInfo> getArticleList();
}
