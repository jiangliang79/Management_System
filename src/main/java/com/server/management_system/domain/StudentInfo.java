package com.server.management_system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-21
 */
@Data
public class StudentInfo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private String name;
    private Integer sex;
    private Long phone;
    private Long classId;
    private Long idCard;
    private Long studentNumber;
    private String nativePlace;
    private String nowPlace;
    private Integer deleted;
    private Long createTime;
    private Long updateTime;
}
