## RNginx(Vertx + JAVA) 

最近因工作需要，学习了异步编程语言Vertx,结合java实现nginx的部分功能，加强自己的理解。

> Vertx 学习资料
> - B站学习视频  两位大牛！
>   - https://space.bilibili.com/24370353 牧云踏歌（本次项目启蒙点，就是根据牧云大牛的视频）
>   - https://space.bilibili.com/8227104  dreamlike_ocean
> - Vertx 文档
>   - https://vertx-china.gitee.io/ 中文文档
>   - https://github.com/vert-x3/vertx-examples 官方案例


#### 项目计划
- [X] 反向代理  -  2022/6/29 ~ 2022/7/1
- [X] 负载均衡  -  2022/6/30 ~ 2022/7/1
- [X] 动静分离  -  2022/6/30 ~ 2022/6/30


#### 更新日志
- 2022/6/29 初步实现简易反向代理及配置 
- 2022/6/30 
    - 重构反向代理 
    - 实现动静分离 配置
    - 实现负载均衡 --- 轮询
- 2022/7/1
    - 增加负载均衡算法
        - 随机/轮询/权重/IPHASH
        
#### 项目介绍
- 动静分离/反向代理:
![动静分离](img/动静分离.png) ![反向代理](img/反向代理.png)
- 负载均衡
![负载均衡](img/负载均衡.png)
- RNginx Vs Nginx  (胜利！！！)
    - GET
![vs](img/vs.png)
    - POST
  ![vs](img/post_Vs.png)


#### 未来计划
- [ ] 第一版有些地方有点臃肿，后续再来优化吧
- [ ] 增加加密/解密过程  