package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.ClassInfo;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-23
 */
@Mapper
public interface ClassInfoRepository extends BaseMapper<ClassInfo> {
    @Select("select * from class_info where deleted = 0")
    List<ClassInfo> getClassList();

    @Select("select * from class_info where profession_id = #{professionId} and deleted = 0")
    List<ClassInfo> selectByProfessionId(@Param("professionId") Long professionId);

    @Select("select * from class_info where college_id = #{collegeId} and deleted = 0")
    List<ClassInfo> selectByCollegeId(@Param("collegeId") Long collegeId);

    @Select("select * from class_info where id = #{id} and deleted = 0 limit 1")
    ClassInfo selectByClassId(@Param("id") Long classId);

    @Select("select * from class_info where name = #{name} and college_id = #{collegeId} and profession_id = "
            + "#{professionId} and deleted = 0 limit 1")
    ClassInfo selectByNameAndProfessionIdAndCollegeId(@Param("name") String name, @Param("collegeId") Long collegeId,
            @Param("professionId") Long professionId);
}
