# data-management-platform
四川农业大学第四届物联网设计大赛网站设计模块参赛作品。

# 项目介绍
一个服务于学生和老师资源管理平台，方便学校师生的资源分享、作品展示、公告通知。项目基于Springboot + Bootstrap实现，后台服务着力于权限、安全、文件管理等方面，全部页面基于响应式设计。

## 演示地址
* [resource.beifengtz.com](http://resource.beifengtz.com)
 * 测试账号：test
 * 密码：test123456

## 技术栈
* 后台：
  * Springboot + Redis + Shiro + Mybatis + druid + MySQL
* 前端：
  * BootStrap + JQuery + Echarts

## 功能
* 基本功能：
  * 作品展示
  * 文件上传、下载
  * 权限管理
  * 公告发布及管理
* 安全：
  * XSS、SQL防护
  * Dos攻击防护
  * 文件保护
# 系统设计及应用
* 整体

![整体设计](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/E%2BResource.png)

* 前端

![前端](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/E%2BResource%EF%BC%88%E5%89%8D%E7%AB%AF%EF%BC%89.png)

* 后台

![后台](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/E%2BResource%EF%BC%88%E5%90%8E%E5%8F%B0%EF%BC%89.png)

* 数据库

![数据](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/resource_platform_db_design.png)

# 运行源码
## 环境准备
* Java环境：Jdk 1.8.0_144 +
* 缓存数据库：Redis 3.2.100 +
* 数据库：Mysql 5.7.0 +
* 操作系统：Windows server / Linux
* 依赖包管理工具：Apache-maven-3.5.3 +
* 开发工具：idea
## 系统配置
### 一、 用idea打开项目
打开目录src\main\resources，可以看到有四个yml文件

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/1.png)

  * application.yml是系统公共配置文件
  * application-dev.yml是系统生产环境配置文件
  * application-test.yml是系统测试环境配置文件
  
在不同环境打包运行时只需在不同配置文件做相应的配置，以下所有说明以测试环境为例。
### 二、 环境配置
打开application-test.yml文件
1. server.port为系统运行端口号

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/2.png)

2. logging.file为日志文件保存路径

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/3.png)

3. spring.datasource为数据库相应配置（在运行前需要修改为相应环境的数据库）

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/4.png)

4. spring.redis为缓存数据库的相应配置（在运行前需要修改为相应环境的缓存数据库）

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/5.png)

5.	最大文件上传限制和最大请求大小限制

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/6.png)

6.	资源文件和图片文件保存路径配置，要注意不同运行环境的磁盘地址不同，例如：
* Windows运行环境下的配置：

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/7.png)

* Linux运行环境下的配置：

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/8.png)

### 三、公共配置
打开application.yml文件
1.	运行环境选择，测试环境将active改为test，生产环境将其改为dev

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/9.png)

2.	系统时区配置（不做修改）

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/10.png)

3. 文件大小及类型限制

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/11.png)

## 数据库导入
可以使用sqlyogent等数据库管理工具，也可以用命令行导入，这里就不过多赘述了。
## idea测试运行
首先要启动redis-server

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/12.png)

选择好运行的环境，然后直接运行，当控制台出现“beifeng”字样表示运行成功。

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/13.png)

在浏览器中输入ip：端口即可访问，例如测试环境输入http://localhost/（或http://127.0.0.1）

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/141.png)

## 打包
在application.yml文件中选择你要部署的环境，然后通过Maven进行打包

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/15.png)

打包成功后会在target目录下生成一个jar包

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/16.png)

## 运行
同样首先运行redis-server

打开控制台，进入jar包所在目录，然后输入命令（“java –jar data-management-platform-1.0.0.jar”）即可运行，出现“beifeng”字样即表示成功运行。

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/17.png)

# 系统截图
* 登录、注册

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204056.png)

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204107.png)

* 首页

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204249.png)

* 资源浏览

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204425.png)

* 查看资源

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204333.png)

* 查看文章

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204507.png)

* 关于

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204549.png)

* 个人中心

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204636.png)

* 信息修改

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204716.png)

* 文章发布

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204752.png)

* 资源上传

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204833.png)

* 公告管理

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204917.png)

* 用户管理

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226204954.png)

* 资源管理

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226205031.png)

* 权限管理

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226205107.png)

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226205120.png)

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226205131.png)

* 中评率刷新短时间内拦截资源

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226205255.png)

* 高频率刷新ip禁用

![高频率刷新ip禁用](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226174913.png)

* 文件资源保护（直接获取文件路径进行下载而未经过系统认证，进行拦截）

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226205437.png)

![](https://vr360-beifengtz.oss-cn-beijing.aliyuncs.com/github/data-management-platform/20181226205509.png)

如果你觉得还不错的话给颗星星吧~~ 
