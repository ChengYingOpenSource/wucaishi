五彩石
=======

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**五彩石是一个快速数据化查询能力构建平台**

五彩石是面向前后端，致力于解决构建快速化查询能力。开发者只需要通过简单的配置即可快速链接组合需要的业务数据。

## 背景

技术层面面临的痛点问题：

复杂化的RPC接口聚集
QUERY模式下的纯粹代码搬砖
服务端手工聚合模式下的性能瓶颈
RPC接口健康监测机制的缺失
多语言环境下非后端开发者的快速数据获取需求
那么...

**有没有这么一款产品？它可以！**

直面JDBC/HTTP/ES/REDIS等等数据源，做到直查
拖拖拽拽搞定数据组装
天生支持数据源数据并发获取
实时监测目标数据源的健康度，保证服务稳定运行
我需要一个数据，但是我不需要等后端排期！

## 功能特点

- **数据源**
    - 支持多种 JDBC 数据源
    - 支持 HTTP 数据源

- **脚本引擎**
    - 支持 Groovy 脚本
  
- **插件体系**
    - 支持自定义数据源以及自定义脚本引擎

- **资源可检测可限制**
    - 脚本执行资源受限制，防止影响主程序运行

文档
=============
请参照 [五彩石用户手册](https://github.com/ChengYingOpenSource/wucaishi/).

获取帮助
============

![二维码](https://github.com/ChengYingOpenSource/wucaishi/raw/master/resources/dingtalk.JPG)

License
============
请参照 [LICENSE](https://github.com/ChengYingOpenSource/wucaishi/blob/master/LICENSE) 文件