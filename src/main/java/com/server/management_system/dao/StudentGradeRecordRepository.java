package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.StudentGradeRecord;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-26
 */
@Mapper
public interface StudentGradeRecordRepository extends BaseMapper<StudentGradeRecord> {
    @Select("select * from student_grade_record where deleted = 0")
    List<StudentGradeRecord> selectAllList();

    @Select("select * from student_grade_record where deleted = 0 and id = #{recordId} limit 1")
    StudentGradeRecord selectByRecordId(@Param("recordId") Long recordId);

    @Select("select * from student_grade_record where teacher_id = #{teacherId} and deleted = 0")
    List<StudentGradeRecord> selectByTeacherId(@Param("teacherId") Long teacherId);

    @Select("select * from student_grade_record where student_id = #{studentId} and deleted = 0")
    List<StudentGradeRecord> selectByStudentId(@Param("studentId") Long studentId);

    @Select("<script> select * from student_grade_record where deleted = 0 and class_id in <foreach collection ='ids' "
            + "item='id' open='(' separator=',' close=')'> #{id} </foreach> </script>")
    List<StudentGradeRecord> selectByClassIds(@Param("ids") List<Long> classIds);
}
