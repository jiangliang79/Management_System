package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.CollegeInfo;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-22
 */
@Mapper
public interface CollegeInfoRepository extends BaseMapper<CollegeInfo> {
    @Select("select * from college_info where deleted = 0")
    List<CollegeInfo> getCollegeList();

    @Select("select * from college_info where deleted = 0 and college_id = #{id}")
    CollegeInfo selectByCollegeId(@Param("id") Long id);
}
