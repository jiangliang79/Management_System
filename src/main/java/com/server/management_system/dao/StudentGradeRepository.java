package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.StudentGrade;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-03-02
 */
@Mapper
public interface StudentGradeRepository extends BaseMapper<StudentGrade> {
    @Select("select * from student_grade where deleted = 0")
    List<StudentGrade> selectListAll();
}
