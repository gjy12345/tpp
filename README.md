# 描述 Springboot2 + vue element admin + 高仿淘票票小程序
写这个项目的目的一是为了学习 vue2.0 相关的知识点。二是期末大作业。  

使用步骤：
- 导入数据库
- 启动后端接口，APP以及Admin
- 启动web管理端：参考https://github.com/PanJiaChen/vue-element-admin
- 将微信小程序导入微信开发者工具

需要环境：
- jdk8
- maven
- idea
- node
- webstorm(也可以用idea)
- redis
- mysql>=8.0 (也可以用5.x版本，如果导入sql出现报错，需要对sql里出现的编码替换成5.x所支持的编码)

注: 后端接口统一封装消息体在cn.gjyniubi.cinema.common.advice.JsonResultAdvice