> 项目是从 [yiiu](https://github.com/yiiu-co/yiiu) fork，使用的版本为2.7
> 
> 感谢yiiu项目的贡献

## 初衷

以项目或者主要是项目的小企业在项目上线、培训、运维时候通常会依赖现场培训、QQ群、微信群等，此项目主要希望通过在线帮助社区的方式解决项目后期更新版本手册或者处理日常非紧急性的问题。

## 特性

在yiiu2.7项目的基础，修改或增加了以下内容：

- 用户信息增加了电话、QQ，区分了账户和昵称，并修改前端展示为昵称而非账户，关联修改了注册、用户添加等页面。
- 用户管理页面添加了业务系统的筛选，增加了从接口导入用户。
- 去除了第三方登录的功能
- 登录页面去除了统计信息，增加了宣传图片展示
- 注册完成后需要审核，增加了注册审核页面。增加注册完成页面。
- 增加了用户业务系统权限配置（业务使用使用原模块代替），关联修改了所有话题查询页面权限的筛选。
- 积分修改为了活跃度（前端展示的修改）
- QQ号码点击调用QQ
- 后台仪表盘增加了热度分布、业务系统话题分布、访问趋势图表。



# 以下是yiiu原文（打包运行、配置及相关异常处理）
## 快速开始

*数据库里的表是项目启动时自动创建的，不要再问创建表的脚本在哪了*

- 创建数据库yiiu, 字符集utf8，如果想支持emoji，就要选择utf8mb4字符集（仅限使用MySQL数据库）
- `git clone https://github.com/yiiu-co/yiiu`
- 运行 `mvn spring-boot:run` 启动项目 (这一步系统会自动把表创建好)
- 访问 `http://localhost:8080`
- 登录用户名：admin 密码：123456 (权限是超级管理员)

## 打包部署开发环境

- 将项目里的application.yml文件复制一份，重新命名application-prod.yml，并修改里面的配置项
- 运行 `mvn clean compile package`
- 拷贝 `target/yiiu.jar` 到你想存放的地方
- 运行 `java -jar yiiu.jar --spring.profiles.active=prod > yiiu.log 2>&1 &` 项目就在后台运行了
- 关闭服务运行 `ps -ef | grep yiiu.jar | grep -v grep | cut -c 9-15 | xargs kill -s 9`
- 查看日志运行 `tail -200f yiiu.log`

## docker镜像部署

**感谢 @beldon 提供的Dockerfile**

- docker pull tomoya92/yiiu:2.7
- docker run -d --name yiiu -v /var/yiiu:/app/ -p 8080:8080 tomoya92/yiiu:2.7

#### 启动好后可能会报404错误，两个解决办法

1. 把pom.xml里的这段代码放开注释重新打包再启动即可
```
<resource>
  <directory>views</directory>
</resource>
```
2. 把源码里的views文件夹复制到打好的jar包文件夹里（跟jar包同级）

windows上启动脚本参见 [传送门](https://github.com/yiiu-co/yiiu/wiki/windows上的启动脚本)

## 添加emoji支持（仅MySQL数据库）

- 创建数据库时选择 `utf8mb4` 字符集
- 添加下面这段配置到 `/etc/mysql/mysql.conf.d/mysqld.conf` 里的 `[mysqld]` 下，保存重启Mysql服务
```
[mysqld]
character-set-client-handshake = FALSE
character-set-server = utf8mb4
collation-server = utf8mb4_unicode_ci
init_connect='SET NAMES utf8mb4'
```
- 如果不行，试着把yiiu也重启一下

## 切回MySQL方法

打开 `application.yml` 将下面关于mysql连接的配置放开，把sqlite相关的配置注释掉就可以了

```yml
# mysql 配置
  datasource:
    url: jdbc:mysql://localhost/yiiu?useSSL=false&characterEncoding=utf8
    username: root
    password: 123123
  jpa:
    database: mysql
```

```yml
# sqlite 配置
  datasource:
    driver-class-name: org.sqlite.JDBC
    url: jdbc:sqlite:./yiiu.sqlite
  jpa:
    database-platform: co.yiiu.core.dialect.SQLiteDialect
```

另外pom.xml文件里的mysql依赖也要取消注释

```xml
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <scope>runtime</scope>
</dependency>
```

## 配置邮箱

我只配置了QQ邮箱，按照下面配置方法配置是没有问题的

```
mail:
  host: smtp.qq.com # 如果是企业邮箱这里要改成 smtp.exmail.qq.com
  username: xxoo@qq.com # 你的QQ邮箱地址
  password: # 这里的密码是QQ邮箱的授权码
  port: 465
  properties:
    mail.smtp.auth: true
    mail.smtp.ssl.enable: true
    mail.smtp.starttls.enable: true
    mail.smtp.starttls.required: true
```