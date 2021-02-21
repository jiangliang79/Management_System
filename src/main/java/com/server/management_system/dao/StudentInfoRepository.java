package com.server.management_system.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.StudentInfo;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@Mapper
public interface StudentInfoRepository extends BaseMapper<StudentInfo> {
}
