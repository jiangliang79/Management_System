package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.StudentTaskArticle;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-24
 */
@Mapper
public interface StudentTaskArticleRepository extends BaseMapper<StudentTaskArticle> {
    @Select("select * from student_task_article where student_id = #{studentId} and deleted = 0")
    List<StudentTaskArticle> selectByStudentId(@Param("studentId") Long studentId);
}
