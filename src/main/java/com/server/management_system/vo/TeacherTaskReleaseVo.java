package com.server.management_system.vo;

import lombok.Data;

/**
 * @author jiangliang <jiangliang@kuaishou.com>
 * Created on 2021-02-25
 */
@Data
public class TeacherTaskReleaseVo {
    private Long recordId;
    private Long articleId;
    private String articleName;
    private Long releaseTime;
    private Long teacherId;
    private String teacherName;
    private String collegeName;
    private String professionName;
    private String className;
}
