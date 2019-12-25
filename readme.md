#学成在线微服务开发日志
##20191222 基础工程搭建
1. 项目导入
    1. parent：父工程
    2. common：通用组件
    2. model：domain实体类
    4. util：工具类
    5. api：接口
2. UI项目初探
### 遇到的问题
1. MongoDB服务启动失败。
    * 解决办法：
        * https://www.jianshu.com/p/49cb69a64187
        
        
## 20191224 
1. mongodb数据可入门
2. cms数据库导入
3. 页面查询接口定义
4. 创建cms服务工程-cms工程结构
### 遇到的问题
1. 项目依赖飘红
    * 原因：由于从pdf中复制pom内容，其中短横`-`格式或编码不对，导致maven无法识别依赖
        * 解决方法：删除短横`-`，重新键盘输入即可
        
2. springboot启动：找不到或无法加载主类
    * 原因：应该是没有正确install
        * 解决方法：clean，重新install后再启动
3. 多模块mvn package失败
    * 原因：parent工程中配置了`spring-boot-maven-plugin`插件，不应该给parent添加这个构建插件，而应该给终端项目使用，因为这个插件的 repackage 目标会处理 jar 包，导致依赖它的模块无法使用它。在 parent 项目中使用它会导致每个子项目都执行了该目标，进而出现编译失败。via：https://my.oschina.net/tridays/blog/825245
4. 关于spring cloud中Feign导入依赖为unknow的情况
    * 原因：不详
        * 解决方法：把`spring-cloud-starter-feign`变为`spring-cloud-starter-openfeign`。via: https://blog.csdn.net/weixin_42740268/article/details/84822260