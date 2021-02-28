package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.TeacherClassRelation;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-23
 */
@Mapper
public interface TeacherClassRelationRepository extends BaseMapper<TeacherClassRelation> {
    @Select("select * from teacher_class_relation where deleted = 0")
    List<TeacherClassRelation> selectAll();

    @Select("select * from teacher_class_relation where deleted = 0 and teacher_id = #{teacherId}")
    List<TeacherClassRelation> selectByTeacherId(@Param("teacherId") Long teacherId);

    @Select("select * from teacher_class_relation where deleted = 0 and class_id = #{classId}")
    List<TeacherClassRelation> selectByClassId(@Param("classId") Long classId);

    @Select("select * from teacher_class_relation where deleted = 0 and id = #{id} limit 1")
    TeacherClassRelation selectByRecordId(@Param("id") Long id);

    @Select("select * from teacher_class_relation where deleted = 0 and teacher_id = #{teacherId} and class_id = "
            + "#{classId} limit 1")
    TeacherClassRelation selectByTeacherIdAndClassId(@Param("teacherId") Long teacherId,
            @Param("classId") Long classId);
}
