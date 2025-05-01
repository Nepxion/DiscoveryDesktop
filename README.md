![](https://nepxion.github.io/Discovery/docs/discovery-doc/Banner.png)

# Discovery【探索】云原生微服务解决方案
![Total visits](https://visitor-badge.laobi.icu/badge?page_id=Nepxion&title=total%20visits)  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/Nepxion/Discovery/blob/6.x.x/LICENSE)  [![Maven Central](https://img.shields.io/maven-central/v/com.nepxion/discovery.svg?label=maven)](https://search.maven.org/artifact/com.nepxion/discovery)  [![Javadocs](http://www.javadoc.io/badge/com.nepxion/discovery-plugin-framework-starter.svg)](http://www.javadoc.io/doc/com.nepxion/discovery-plugin-framework-starter)  [![Build Status](https://github.com/Nepxion/Discovery/workflows/build/badge.svg)](https://github.com/Nepxion/Discovery/actions)  [![Codacy Badge](https://app.codacy.com/project/badge/Grade/5c42eb719ef64def9cad773abd877e8b)](https://www.codacy.com/gh/Nepxion/Discovery/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Nepxion/Discovery&amp;utm_campaign=Badge_Grade)  [![Stars](https://img.shields.io/github/stars/Nepxion/Discovery.svg?label=Stars&style=flat&logo=GitHub)](https://github.com/Nepxion/Discovery/stargazers)  [![Stars](https://gitee.com/Nepxion/Discovery/badge/star.svg?theme=gvp)](https://gitee.com/Nepxion/Discovery/stargazers)

[![Wiki](https://badgen.net/badge/icon/wiki?icon=wiki&label=GitHub)](https://github.com/Nepxion/Discovery/wiki)  [![Wiki](https://badgen.net/badge/icon/wiki?icon=wiki&label=Gitee)](https://gitee.com/nepxion/Discovery/wikis/pages?sort_id=3993615&doc_id=1124387)  [![Discovery PPT](https://img.shields.io/badge/Discovery%20-ppt-brightgreen?logo=Microsoft%20PowerPoint)](https://nepxion.github.io/Discovery/docs/link-doc/discovery-ppt.html)  [![Discovery Page](https://img.shields.io/badge/Discovery%20-page-brightgreen?logo=Microsoft%20Edge)](https://nepxion.github.io/Discovery/)  [![Discovery Platform Page](https://img.shields.io/badge/Discovery%20Platform%20-page-brightgreen?logo=Microsoft%20Edge)](https://nepxion.github.io/DiscoveryPlatform)  [![Discovery Desktop Page](https://img.shields.io/badge/Discovery%20Desktop%20-page-brightgreen?logo=Microsoft%20Edge)](https://nepxion.github.io/DiscoveryDesktop)  [![Polaris Page](https://img.shields.io/badge/Polaris%20-page-brightgreen?logo=Microsoft%20Edge)](https://polaris-paas.github.io/polaris-wiki)

<a href="https://github.com/Nepxion" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery/docs/icon-doc/github.png"></a>&nbsp;  <a href="https://gitee.com/Nepxion" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery/docs/icon-doc/gitee.png"></a>&nbsp;  <a href="https://search.maven.org/search?q=g:com.nepxion" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery/docs/icon-doc/maven.png"></a>&nbsp;  <a href="https://nepxion.github.io/Discovery/docs/contact-doc/wechat.jpg" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery/docs/icon-doc/wechat.png"></a>&nbsp;  <a href="https://nepxion.github.io/Discovery/docs/contact-doc/dingding.jpg" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery/docs/icon-doc/dingding.png"></a>&nbsp;  <a href="https://nepxion.github.io/Discovery/docs/contact-doc/gongzhonghao.jpg" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery/docs/icon-doc/gongzhonghao.png"></a>&nbsp;  <a href="mailto:1394997@qq.com" tppabs="#"><img width="25" height="25" src="https://nepxion.github.io/Discovery/docs/icon-doc/email.png"></a>

如果您觉得本框架具有一定的参考价值和借鉴意义，请帮忙在页面右上角 [**Star**]

## 简介

### 作者简介
- Nepxion开源社区创始人
- 2020年阿里巴巴中国云原生峰会出品人
- 2020年被Nacos和Spring Cloud Alibaba纳入相关开源项目
- 2021年阿里巴巴技术峰会上海站演讲嘉宾
- 2021年荣获陆奇博士主持的奇绩资本，进行风险投资的关注和调研
- 2021年入选Gitee最有价值开源项目
- 2024年入围中国开源创新榜候选项目
- 阿里巴巴官方书籍《Nacos架构与原理》作者之一
- Spring Cloud Alibaba Steering Committer、Nacos Group Member
- Spring Cloud Alibaba、Nacos、Sentinel、OpenTracing Committer & Contributor

<img src="https://nepxion.github.io/Discovery/docs/discovery-doc/CertificateGVP.jpg" width="43%"><img src="https://nepxion.github.io/Discovery/docs/discovery-doc/AwardNacos1.jpg" width="28%"><img src="https://nepxion.github.io/Discovery/docs/discovery-doc/AwardSCA1.jpg" width="28%">

### 商业合作
① Discovery系列

| 框架名称 | 框架版本 | 支持Spring Cloud版本 | 使用许可 |
| --- | --- | --- | --- |
| Discovery | 1.x.x ~ 6.x.x | Camden ~ Hoxton | 开源，永久免费 |
| DiscoveryX | 7.x.x + | 2020 + | 闭源，商业许可 |

② Polaris系列

Polaris为Discovery高级定制版，特色功能

- 基于Nepxion Discovery集成定制
- 多云、多活、多机房流量调配
- 跨云动态域名、跨环境适配
- DCN、DSU、SET单元化部署
- 组件灵活装配、配置对外屏蔽
- 极简低代码PaaS平台

| 框架名称 | 框架版本 | 支持Discovery版本 | 支持Spring Cloud版本 | 使用许可 |
| --- | --- | --- | --- | --- |
| Polaris | 1.x.x | 6.x.x | Finchley ~ Hoxton | 闭源，商业许可 |
| Polaris | 2.x.x | 7.x.x + | 2020 + | 闭源，商业许可 |

有商业版需求的企业和用户，请添加微信1394997，联系作者，洽谈合作事宜

### 入门资料
![](https://nepxion.github.io/Discovery/docs/discovery-doc/Logo64.png) Discovery【探索】企业级云原生微服务开源解决方案

① 快速入门
- [快速入门Github版](https://github.com/Nepxion/Discovery/wiki)
- [快速入门Gitee版](https://gitee.com/Nepxion/Discovery/wikis/pages)

② 解决方案
- [解决方案WIKI版](http://nepxion.com/discovery)
- [解决方案PPT版](https://nepxion.github.io/Discovery/docs/link-doc/discovery-ppt.html)

③ 最佳实践
- [最佳实践PPT版](https://nepxion.github.io/Discovery/docs/link-doc/discovery-ppt-1.html)

④ 平台桌面
- [平台界面WIKI版](http://nepxion.com/discovery-platform)
- [图形桌面WIKI版](http://nepxion.com/discovery-desktop)

⑤ 框架源码
- [框架源码Github版](https://github.com/Nepxion/Discovery)
- [框架源码Gitee版](https://gitee.com/Nepxion/Discovery)

⑥ 指南示例源码
- [指南示例源码Github版](https://github.com/Nepxion/DiscoveryGuide)
- [指南示例源码Gitee版](https://gitee.com/Nepxion/DiscoveryGuide)

⑦ 指南示例说明
- Spring Cloud Finchley ~ Hoxton版本
    - [极简版指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/6.x.x-simple)，分支为6.x.x-simple
    - [极简版域网关部署指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/6.x.x-simple-domain-gateway)，分支为6.x.x-simple-domain-gateway
    - [极简版非域网关部署指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/6.x.x-simple-non-domain-gateway)，分支为6.x.x-simple-non-domain-gateway
    - [集成版指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/6.x.x)，分支为6.x.x
    - [高级版指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/6.x.x-complex)，分支为6.x.x-complex
- Spring Cloud 20xx版本
    - [极简版指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/master-simple)，分支为master-simple
    - [极简版本地化指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/master-simple-native)，分支为master-simple-native
    - [集成版指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/master)，分支为master

![](https://nepxion.github.io/Discovery/docs/polaris-doc/Logo64.png) Polaris【北极星】企业级云原生微服务商业解决方案

① 解决方案
- [解决方案WIKI版](http://nepxion.com/polaris)

② 框架源码
- [框架源码Github版](https://github.com/polaris-paas/polaris-sdk)
- [框架源码Gitee版](https://gitee.com/polaris-paas/polaris-sdk)

③ 指南示例源码
- [指南示例源码Github版](https://github.com/polaris-paas/polaris-guide)
- [指南示例源码Gitee版](https://gitee.com/polaris-paas/polaris-guide)

④ 指南示例说明
- Spring Cloud Finchley ~ Hoxton版本
    - [指南示例](https://github.com/polaris-paas/polaris-guide/tree/1.x.x)，分支为1.x.x
- Spring Cloud 20xx版本
    - [指南示例](https://github.com/polaris-paas/polaris-guide/tree/master)，分支为master

### 请联系我
微信、钉钉、公众号和文档

![](https://nepxion.github.io/Discovery/docs/contact-doc/wechat-1.jpg)![](https://nepxion.github.io/Discovery/docs/contact-doc/dingding-1.jpg)![](https://nepxion.github.io/Discovery/docs/contact-doc/gongzhonghao-1.jpg)![](https://nepxion.github.io/Discovery/docs/contact-doc/document-1.jpg)

## 目录
- [简介](#简介)
    - [作者简介](#作者简介)
    - [商业合作](#商业合作)
    - [入门资料](#入门资料)
    - [功能概述](#功能概述)
    - [郑重致谢](#郑重致谢)
    - [请联系我](#请联系我)
- [环境搭建](#环境搭建)
    - [获取控制台](#获取控制台)
    - [启动控制台](#启动控制台)
    - [获取图形化桌面端](#获取图形化桌面端)
    - [启动图形化桌面端](#启动图形化桌面端)
    - [登录图形化桌面端](#登录图形化桌面端)
- [全链路编排建模](#全链路编排建模)
    - [全链路蓝绿发布编排建模](#全链路蓝绿发布编排建模)
    - [全链路灰度发布编排建模](#全链路灰度发布编排建模)
- [全链路流量侦测](#全链路流量侦测)
    - [全链路蓝绿发布流量侦测](#全链路蓝绿发布流量侦测)
    - [全链路灰度发布流量侦测](#全链路灰度发布流量侦测)
    - [全链路蓝绿灰度发布混合流量侦测](#全链路蓝绿灰度发布混合流量侦测)
- [Star走势图](#Star走势图)

## 环境搭建

### 获取控制台
控制台获取方式有两种方式
- 通过[https://github.com/Nepxion/DiscoveryTool/releases](https://github.com/Nepxion/DiscoveryTool/releases)下载最新版本的console
- 编译[https://github.com/Nepxion/DiscoveryTool](https://github.com/Nepxion/DiscoveryTool)，拉取对应Spring Boot版本的分支，在target目录下产生discovery-console-release

### 启动控制台
- 导入IDE或者编译成Spring Boot程序运行
- 运行之前，先修改src/main/resources/bootstrap.properties的相关配置，包括注册中心和配置中心的地址等
- 在Windows操作系统下，运行startup.bat，在Mac或者Linux操作系统下，运行startup.sh

### 获取图形化桌面端
桌面端获取方式有两种方式
- 通过[https://github.com/Nepxion/DiscoveryDesktop/releases](https://github.com/Nepxion/DiscoveryDesktop/releases)下载最新版本的desktop
- 编译[https://github.com/Nepxion/DiscoveryDesktop](https://github.com/Nepxion/DiscoveryDesktop)，在target目录下产生discovery-desktop-release

### 启动图形化桌面端
- 导入IDE或者编译成应用程序运行
- 修改config/console.properties中的url，指向控制台的地址
- 在Windows操作系统下，运行startup.bat，在Mac或者Linux操作系统下，运行startup.sh

### 登录图形化桌面端
登录认证，用户名和密码为admin/admin或者nepxion/nepxion。控制台支持简单的认证，用户名和密码配置在上述控制台的bootstrap.properties中，使用者可以自己扩展AuthenticationResource并注入，实现更专业的认证功能

 ![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop8.jpg)

## 全链路编排建模
全链路编排建模工具，只提供最经典和最常用的蓝绿灰度发布场景功能，并不覆盖框架所有的功能

### 全链路蓝绿发布编排建模
![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop9.jpg)

① 导航栏上选择〔全链路服务蓝绿发布〕

② 〔全链路服务蓝绿发布〕界面的工具栏上，点击【新建】按钮，弹出【新建配置】对话框。确认下面选项后，点击【确定】按钮后，进行全链路蓝绿发布编排建模

- 〔订阅参数〕项。选择〔局部订阅〕或者〔全局订阅〕，通过下拉菜单〔订阅组名〕和〔订阅服务名〕，〔订阅服务名〕可以选择网关（以网关为发布入口）或者服务（以服务为发布入口）。如果是〔全局订阅〕，则不需要选择〔订阅服务名〕
- 〔部署参数〕项。选择〔域网关模式〕（发布界面上提供只属于〔订阅组〕下的服务列表）或者〔非域网模式〕（发布界面上提供所有服务列表）
- 〔发布策略〕项。选择〔版本策略〕或者〔区域策略〕
- 〔路由类型〕项。选择〔蓝 | 绿 | 兜底〕或者〔蓝 | 兜底〕

③ 在〔蓝绿条件〕中，分别输入〔蓝条件〕和〔绿条件〕

- 〔蓝条件〕输入a==1
- 〔绿条件〕输入a==1&&b==2

使用者可以通过〔条件校验〕来判断条件是否正确。例如，在〔绿条件〕区的校验文本框里，输入a=1，执行校验，将提示〔校验结果:false〕，输入a=1;b=2，将提示〔校验结果:true〕

④ 在〔蓝绿编排〕中，分别选择如下服务以及其版本，并点击【添加】按钮，把路由链路添加到拓扑图上

- 服务discovery-guide-service-a，〔蓝版本〕=1.1，〔绿版本〕=1.0，〔兜底版本〕=1.0
- 服务discovery-guide-service-b，〔蓝版本〕=1.1，〔绿版本〕=1.0，〔兜底版本〕=1.0

![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop10.jpg)

⑤ 如果希望内置Header参数，可以〔蓝绿参数〕的文本框中输入

⑥ 全链路编排建模完毕，点击工具栏上【保存】按钮进行保存，也可以先点击【预览】按钮，在弹出的【预览配置】对话框中，确认规则策略无误后再保存。使用者可以访问Nacos界面查看相关的规则策略是否已经存在

![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop11.jpg)

⑦ 对于已经存在的策略配置，可以通过点击工具栏上【打开】按钮，在弹出的【打开配置】对话框中，根据上述逻辑相似，确定〔订阅参数〕项后，选择〔打开远程配置〕（载入Nacos上对应的规则策略）或者〔打开本地配置〕（载入本地硬盘上规则策略文件rule.xml）

⑧ 对于已经存在的策略配置，如果想重置清除掉，点击工具栏上【重置】按钮进行重置清除

### 全链路灰度发布编排建模
① 导航栏上选择〔全链路服务灰度发布〕

![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop13.jpg)

② 在〔灰度条件〕中，〔灰度条件〕（灰度流量占比）选择95%，〔稳定条件〕（稳定流量占比）会自动切换成5%

其它步骤跟[全链路蓝绿发布编排建模](#全链路蓝绿发布编排建模)相似，但比其简单

![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop14.jpg)

## 全链路流量侦测

### 全链路蓝绿发布流量侦测
① 导航栏上选择〔全链路服务流量侦测〕

② 在〔侦测入口〕中，操作如下

- 〔类型〕项。选择〔网关〕或者〔服务〕，本示例的规则策略是配置在网关上，所以选择〔网关〕
- 〔协议〕项。选择〔http://〕或者〔https://〕，视网关或者服务暴露出来的协议类型而定，本示例暴露出来的是http协议，所以选择〔http://〕
- 〔服务〕项。选择一个网关名或者服务名，下拉菜单列表随着〔类型〕项的改变而改变，蓝绿发布规则策略是配置在discovery-guide-gateway上，所以选择它
- 〔实例〕项。选择一个网关实例或者服务实例的IP地址和端口，下拉菜单列表随着〔服务〕的改变而改变

③ 在〔侦测参数〕中，操作如下

添加〔Header〕项和〔Parameter〕项，也可以〔Cookie〕项，使用者可以任意选择2个

- 〔Header〕项。输入a=1
- 〔Parameter〕项。输入b=2

④ 在〔侦测链路〕中，操作如下

- 增加服务discovery-guide-service-a
- 增加服务discovery-guide-service-b

⑤ 在〔侦测执行〕中，操作如下

- 〔维护〕项。选择〔版本〕、〔区域〕、〔环境〕、〔可用区〕、〔地址〕或者〔组〕，维护表示在拓扑图上聚合调用场景的维度，本示例的规则策略是是基于版本维度进行发布，所以选择〔版本〕
- 〔次数〕项。选择执行侦测的次数，基于网关和服务的性能压力，使用者需要酌情考虑调用次数
- 〔次数〕项。选择执行侦测的同一时刻线程并发数，并发数是对于图形化桌面端而言的
- 〔成功〕项。用来显示侦测成功的百分比
- 〔失败〕项。用来显示侦测失败的百分比
- 〔耗时〕项。用来显示侦测执行的消耗时间

⑥ 点击工具栏上【开始】按钮开始侦测，在侦测执行过程中，可以点击工具栏上【停止】按钮停止侦测

![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop15.jpg)

从上述截图中，可以看到

- 在条件a==1&&b==2的〔绿条件〕下，执行〔网关〕->〔a服务1.0版本〕->〔b服务1.0版本〕的〔绿路由〕

⑦ 点击工具栏上【查看】按钮查看拓扑图上所有节点配置的规则策略，包括局部配置和全局配置

![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop16.jpg)

⑧ 支持直接n-d-version策略路由Header驱动的蓝绿发布流量侦测

![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop7.jpg)

### 全链路灰度发布流量侦测
① 导航栏上选择〔全链路服务流量侦测〕

② 在〔侦测入口〕中，操作如下

- 〔服务〕项。灰度发布规则策略是配置在discovery-guide-zuul上，所以选择它

③ 在〔侦测参数〕中，不需要输入任何值

④ 在〔侦测执行〕中，〔次数〕项的值越大，灰度权重百分比越准确

其它步骤跟[全链路蓝绿发布流量侦测](#全链路蓝绿发布流量侦测)相似，但比其简单

![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop17.jpg)

从上述截图中，可以看到

- 执行〔网关〕->〔a服务1.1版本〕->〔b服务1.1版本〕的〔灰度路由〕权重百分比95%左右
- 执行〔网关〕->〔a服务1.0版本〕->〔b服务1.0版本〕的〔稳定路由〕权重百分比5%左右

### 全链路蓝绿灰度发布混合流量侦测
① 全链路蓝绿发布 + 灰度发布混合模式下流量侦测

在网关上配置了蓝绿发布规则策略，在a服务上配置了灰度发布规则策略

![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop5.jpg)

② 全链路灰度发布 + 蓝绿发布混合模式下流量侦测

在网关上配置了灰度发布规则策略，在a服务上配置了蓝绿发布规则策略

![](https://nepxion.github.io/DiscoveryDesktop/docs/discovery-doc/DiscoveryDesktop6.jpg)

## Star走势图
[![Stargazers over time](https://starchart.cc/Nepxion/Discovery.svg)](https://starchart.cc/Nepxion/Discovery)