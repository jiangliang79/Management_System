package com.server.management_system.dao;

import java.util.List;

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

    @Select("select * from student_info where deleted = 0")
    List<StudentInfo> getStudentList();

    @Select("<script> select * from student_info where deleted = 0 and class_id in <foreach collection ='ids' "
            + "item='id' open='(' separator=',' close=')'> #{id} </foreach> </script>")
    List<StudentInfo> selectStudentListByClassIds(@Param("ids") List<Long> classIds);

    @Select("<script> select * from student_info where deleted = 0 and student_id in <foreach collection ='ids' "
            + "item='id' open='(' separator=',' close=')'> #{id} </foreach> </script>")
    List<StudentInfo> selectStudentListByStudentIds(@Param("ids") List<Long> studentIds);
}
