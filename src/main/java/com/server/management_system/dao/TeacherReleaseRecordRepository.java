package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.TeacherReleaseRecord;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-25
 */
@Mapper
public interface TeacherReleaseRecordRepository extends BaseMapper<TeacherReleaseRecord> {
    @Select("select * from teacher_release_record where teacher_id = #{teacherId} and deleted = 0")
    List<TeacherReleaseRecord> selectByTeacherId(@Param("teacherId") Long teacherId);

    @Select("<script> select * from teacher_release_record where deleted = 0 and class_id in <foreach collection ='ids' "
            + "item='id' open='(' separator=',' close=')'> #{id} </foreach> </script>")
    List<TeacherReleaseRecord> selectByClassIds(@Param("ids") List<Long> classIds);

    @Select("select * from teacher_release_record where deleted = 0")
    List<TeacherReleaseRecord> selectAllList();

    @Select("select * from teacher_release_record where deleted = 0 and id = #{recordId} limit 1")
    TeacherReleaseRecord selectByRecordId(@Param("recordId") Long recordId);
}
