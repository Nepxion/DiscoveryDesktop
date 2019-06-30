# Nepxion DiscoveryUI
[![Total lines](https://tokei.rs/b1/github/Nepxion/DiscoveryUI?category=lines)](https://tokei.rs/b1/github/Nepxion/DiscoveryUI?category=lines)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/Nepxion/DiscoveryUI/blob/master/LICENSE)

## 简介

Nepxion DiscoveryUI is a web framework based on Vue for Nepxion Discovery project [http://www.nepxion.com](http://www.nepxion.com/)

参考[入门教程](https://github.com/Nepxion/Docs/blob/master/discovery-doc/README_QUICK_START.md)

## 准备工作

- node.js环境（npm包管理器）
- vue-cli 脚手架构建工具
- cnpm npm的淘宝镜像

## 编译运行

``` bash
# install dependencies
npm install

# or 建议不要用cnpm  安装有各种诡异的bug 可以通过如下操作解决npm速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# serve with hot reload at localhost:8080
npm run dev

# 打开浏览器访问 http://localhost:8080

# build for production with minification
npm run build

# build for production and view the bundle analyzer report
npm run build --report
```

也可以[下载](https://github.com/Nepxion/DiscoveryUI/releases)已发布版本。可以直接运行nginx，浏览器访问[http://localhost:8080](http://localhost:8080)，可通过nginx配置(conf/nginx.conf)修改端口

## 界面展示

![拓扑图](/images/screenshot-1-1.jpg)

![拓扑图](/images/screenshot-1-2.jpg)

![灰度路由](/images/screenshot-2.png)

![灰度发布](/images/screenshot-3.png)

### 依赖

- vue
- element-ui
- d3
- axios
- mockjs

### 作者

[78552423@qq.com](https://github.com/eshun)

## Star走势图

[![Stargazers over time](https://starchart.cc/Nepxion/DiscoveryUI.svg)](https://starchart.cc/Nepxion/DiscoveryUI)
