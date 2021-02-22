# Management_System
实习管理系统后端项目
# 项目运行
1、下载安装jdk
http://soft.corp.kuaishou.com/ios/jdk-8u202-macosx-x64.dmg   
2、下载安装maven  
```html
（1) brew install maven   
 (2) mkdir -p ~/.m2/
 (3) 在~/.m2/ 目录下创建 settings.xml文件和repository文件夹   
 (4) 将以下内容复制到settings.xml中 
```
 ```html
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
   
    <localRepository>/Users/jiangliang1/.m2/repository_person</localRepository>

    <mirrors>
	<mirror>	
	    <id>alimaven</id>
            <mirrorOf>*</mirrorOf>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public</url>
       </mirror>
    </mirrors>

    <profiles>
         <profile>
            <id>ali</id>
            <repositories>
                <repository>
                    <id>alimaven</id>
                    <name>aliyun maven</name>
                    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>true</enabled>
                    </snapshots>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <id>alimaven</id>
                    <name>aliyun maven</name>
                    <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
                </pluginRepository>
            </pluginRepositories>
    	</profile>
    </profiles>

    <activeProfiles>
        <activeProfile>ali</activeProfile>
    </activeProfiles>

</settings>
```
 (5)下载项目，用idea打开，在idea中配置maven。
 (6)运行ManagementSystemApplication类