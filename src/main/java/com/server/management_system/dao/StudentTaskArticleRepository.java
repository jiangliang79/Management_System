package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.StudentGradeRecord;
import com.server.management_system.domain.StudentTaskArticle;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-24
 */
@Mapper
public interface StudentTaskArticleRepository extends BaseMapper<StudentTaskArticle> {
    @Select("select * from student_task_article where student_id = #{studentId} and deleted = 0")
    List<StudentTaskArticle> selectByStudentId(@Param("studentId") Long studentId);

    @Select("select * from student_task_article where deleted = 0 and status in(0,1)")
    List<StudentTaskArticle> selectByStatus();

    @Select("select * from student_task_article where teacher_id = #{teacherId} and deleted = 0 and status in(0,1)")
    List<StudentTaskArticle> selectByTeacherId(@Param("teacherId") Long teacherId);

    @Select("<script> select * from student_task_article where deleted = 0 and class_id in <foreach collection ='ids' "
            + "item='id' open='(' separator=',' close=')'> #{id} </foreach> </script>")
    List<StudentTaskArticle> selectByClassIds(@Param("ids") List<Long> classIds);
}
