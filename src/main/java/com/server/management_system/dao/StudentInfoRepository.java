package com.server.management_system.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.StudentInfo;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@Mapper
public interface StudentInfoRepository extends BaseMapper<StudentInfo> {
    @Select("select * from student_info where student_id = #{studentId} and deleted = 0 limit 1")
    StudentInfo selectByStudentId(@Param("studentId") Long studentId);
}
