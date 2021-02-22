package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.ProfessionInfo;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-22
 */
@Mapper
public interface ProfessionInfoRepository extends BaseMapper<ProfessionInfo> {
    @Select("select * from profession_info where deleted = 0")
    List<ProfessionInfo> getProfessionList();

    @Select("select * from profession_info where deleted = 0 and college_id = #{collegeId}")
    List<ProfessionInfo> selectByCollegeId(@Param("collegeId") Long collegeId);
}
