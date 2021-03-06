create table user_info
(
    id          bigint unsigned not null auto_increment comment '用户ID',
    username    varchar(128)    not null default '' comment '用户名',
    password    varchar(128)    not null default '' comment '密码',
    token       varchar(255)    not null default '' comment '用户token',
    name        varchar(255)    not null default '' comment '昵称',
    phone       bigint          not null default 0 comment '联系方式',
    type        int             not null default 0  comment '用户类型：1：学生，2：老师，3：二级学院，0：超级管理员',
    description varchar(1024)   not null default '' comment '备注',
    deleted     int             not null default 0  comment '是否删除,1已删除，0未删除',
    create_time bigint          not null default 0  comment '创建时间',
    update_time bigint          not null default 0  comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_username` (`username`),
    KEY `idx_type` (`type`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '用户信息表';

create table college_info
(
    id          bigint unsigned not null auto_increment comment '学院ID',
    college_id  bigint          not null default 0  comment '学院ID',
    name        varchar(128)    not null default '' comment '学院名称',
    description varchar(1024)   not null default '' comment '备注',
    deleted     int             not null default 0  comment '是否删除,1已删除，0未删除',
    create_time bigint          not null default 0  comment '创建时间',
    update_time bigint          not null default 0  comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_name` (`name`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '学院信息表';


create table profession_info
(
    id          bigint unsigned not null auto_increment comment '专业ID',
    name        varchar(128)    not null default '' comment '专业名称',
    college_id  bigint          not null default 0  comment '学院ID',
    description varchar(1024)   not null default '' comment '备注',
    deleted     int             not null default 0  comment '是否删除,1已删除，0未删除',
    create_time bigint          not null default 0  comment '创建时间',
    update_time bigint          not null default 0  comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_college_id_name` (`college_id`,`name`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '专业信息表';

create table class_info
(
    id             bigint unsigned not null auto_increment comment '班级ID',
    name           varchar(128)    not null default '' comment '班级名称',
    college_id     bigint          not null default 0  comment '学院ID',
    profession_id  bigint          not null default 0  comment '专业ID',
    description    varchar(1024)   not null default '' comment '备注',
    deleted        int             not null default 0  comment '是否删除,1已删除，0未删除',
    create_time    bigint          not null default 0  comment '创建时间',
    update_time    bigint          not null default 0  comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_college_id_profession_id_name` (`college_id`,`profession_id`,`name`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '班级信息表';

create table teacher_class_relation
(
    id             bigint unsigned not null auto_increment comment '自增ID',
    teacher_id     bigint          not null default 0 comment '老师ID',
    class_id       bigint          not null default 0  comment '班级ID',
    deleted        int             not null default 0  comment '是否删除,1已删除，0未删除',
    create_time    bigint          not null default 0  comment '创建时间',
    update_time    bigint          not null default 0  comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_teacher_id_class_id` (`teacher_id`,`class_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '教师班级关联表';

create table student_info
(
    id             bigint unsigned not null auto_increment comment '自增ID',
    student_id     bigint          not null default 0      comment '学生ID',
    name           varchar(128)    not null default ''     comment '学生姓名',
    sex            int             not null default 0      comment '性别，0：男生，1：女生',
    phone          bigint          not null default 0      comment  '电话号码',
    class_id       bigint          not null default 0      comment '班级ID',
    id_card        bigint          not null default 0      comment '身份证号',
    student_number bigint          not null default 0      comment '学号',
    native_place   varchar(1024)   not null default ''     comment '籍贯',
    now_place      varchar(1024)   not null default ''     comment '现居住地',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    create_time    bigint          not null default 0      comment '创建时间',
    update_time    bigint          not null default 0      comment '修改时间',
    PRIMARY KEY (id),
    KEY `idx_student_id` (`student_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '学生信息表';

create table article_info
(
    id             bigint unsigned not null auto_increment comment '文档ID',
    name           varchar(255)    not null default 0      comment '文档名称',
    path           varchar(255)    not null default ''     comment '文件路径',
    type           int             not null default 0     comment '文档类型，1:老师评分表，2:学生填写表，3:实习任务表',
    template       int             not null default 0      comment '是否是模板，1：是，0：否',
    start_time     bigint          not null default 0      comment '开始时间',
    end_time       bigint          not null default 0      comment  '结束时间',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '文档信息表';

create table teacher_release_record
(
    id             bigint unsigned not null auto_increment comment '自增ID',
    article_id     bigint          not null default 0      comment '文档ID',
    teacher_id     bigint          not null default 0      comment '教师ID',
    class_id       bigint          not null default 0      comment '班级ID',
    release_time   bigint          not null default 0      comment '发布时间',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '教师发布任务表';

create table student_grade_record
(
    id             bigint unsigned not null auto_increment comment '自增ID',
    article_id     bigint          not null default 0      comment '文档ID',
    student_id     bigint          not null default 0      comment '学生ID',
    teacher_id     bigint          not null default 0      comment '教师ID',
    class_id       bigint          not null default 0      comment '班级ID',
    release_time   bigint          not null default 0      comment '发布时间',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '学生成绩评定表';

create table student_task_article
(
    id             bigint unsigned not null auto_increment comment '自增ID',
    article_id     bigint          not null default 0      comment '文档ID',
    student_id     bigint          not null default 0      comment '学生ID',
    teacher_id     bigint          not null default 0      comment '教师ID',
    class_id       bigint          not null default 0      comment '班级ID',
    status         int             not null default 2      comment '0：通过，1：未通过，2：未填写，3：待批阅',
    remark         varchar(1024)   not null default ''     comment '评语',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    create_time    bigint          not null default 0      comment '创建时间',
    update_time    bigint          not null default 0      comment '修改时间',
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '学生任务表';

create table student_grade
(
    id             bigint unsigned not null auto_increment comment '自增ID',
    student_name   varchar(255)    not null default ''      comment '学生姓名',
    grade          double          not null default 0      comment '学生成绩',
    deleted        int             not null default 0      comment '是否删除,1已删除，0未删除',
    create_time    bigint          not null default 0      comment '创建时间',
    update_time    bigint          not null default 0      comment '修改时间',
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '数据分析学生成绩表';

create table student_attendance
(
    id                  bigint unsigned not null auto_increment comment '自增ID',
    student_name        varchar(255)    not null default ''    comment '学生姓名',
    attendance          double          not null default 0      comment '学生出勤率',
    deleted             int             not null default 0      comment '是否删除,1已删除，0未删除',
    create_time         bigint          not null default 0      comment '创建时间',
    update_time         bigint          not null default 0      comment '修改时间',
    PRIMARY KEY (id)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_bin COMMENT = '数据分析学生出勤率表';

