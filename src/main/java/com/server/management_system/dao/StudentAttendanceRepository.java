package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.StudentAttendance;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-03-02
 */
@Mapper
public interface StudentAttendanceRepository extends BaseMapper<StudentAttendance> {
    @Select("select * from student_attendance where deleted = 0")
    List<StudentAttendance> selectListAll();
}
