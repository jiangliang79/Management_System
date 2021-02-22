package com.server.management_system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.management_system.domain.UserInfo;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-20
 */
@Mapper
public interface UserInfoRepository extends BaseMapper<UserInfo> {
    @Select("select * from user_info where username = #{username} and deleted = 0 limit 1")
    UserInfo selectByUserName(@Param("username") String username);

    @Select("select * from user_info where deleted = 0")
    List<UserInfo> selectUserList();

    @Select("select * from user_info where id = #{userId} and deleted = 0 limit 1")
    UserInfo selectByUserId(@Param("userId") Long userId);
}
