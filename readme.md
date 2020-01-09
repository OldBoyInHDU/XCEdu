# 学成在线微服务开发日志

## 20191222 基础工程搭建

### 1 今日内容

1. 项目导入
    1. parent：父工程
    2. common：通用组件
    2. model：domain实体类
    4. util：工具类
    5. api：接口
2. UI项目初探
### 2 学习总结
1. MongoDB服务启动失败。
    * 解决办法：
        * https://www.jianshu.com/p/49cb69a64187
        
        
## 20191224 

### 1 今日内容

1. mongodb数据可入门
2. cms数据库导入
3. 页面查询接口定义
4. 创建cms服务工程-cms工程结构
### 2 学习总结
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
    
## 20191227

### 1 今日内容

1. 完成并测试了页面查询接口
2. swagger注解的使用 
    1. Swagger2Configuration
        1. `@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增删改查")`
        2. `@ApiOperation("分页查询列表")`
        3. `@ApiImplicitParams({})`
        4. `@ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "path", dataType = "int")`
        5. `@ApiModelProperty("站点ID")`

## 20200106

### 1 今日内容

1. 完成页面自定义查询接口
2. 完成添加页面的接口

### 2 学习总结

#### 自定义条件匹配器

```java
    /**
     * 页面查询方法
     * @param page 传入的第1页，应该是数据库的第0条
     * @param size
     * @param queryPageRequest
     * @return
     */
    @Override
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }

        //自定义条件查询
        //定义条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        exampleMatcher.withMatcher("pageName", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsPage condition = new CmsPage();
        //设置条件值_站点id_模板Id_页面别名
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            condition.setSiteId(queryPageRequest.getSiteId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            condition.setTemplateId(queryPageRequest.getTemplateId());
        }
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            condition.setPageAliase(queryPageRequest.getPageAliase());
        }
        //定义Example
        Example<CmsPage> example = Example.of(condition, exampleMatcher);
        //分页查询
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<CmsPage> pages = cmsPageRepository.findAll(example, pageable);

        QueryResult queryResult = new QueryResult();
        queryResult.setList(pages.getContent());//数据列表
        queryResult.setTotal(pages.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }
```

## 20200107

### 1 今日内容

1. 完成页面编辑的后端代码
2. 完成页面删除的后端代码
3. 编写自定义异常实现异常的处理与前端反馈

### 2 学习总结



