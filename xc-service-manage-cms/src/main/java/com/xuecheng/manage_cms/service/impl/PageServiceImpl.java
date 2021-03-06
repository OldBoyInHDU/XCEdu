package com.xuecheng.manage_cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import com.xuecheng.manage_cms.service.IPageService;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class PageServiceImpl implements IPageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsConfigRepository cmsConfigRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

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

    /*@Override
    public CmsPageResult add(CmsPage cmsPage) {
        //校验 页面名称、站点id、页面webpath的唯一性
        //根据 页面名称、站点id、页面webpath 查cms_page集合，如果查到说明页面已经存在，查不到就继续添加
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1 == null) {
            //调用dao新增页面
            cmsPage.setPageId(null);//保证mongodb帮助我们设置id，清空自己设的id
            CmsPage save = cmsPageRepository.save(cmsPage);
            return new CmsPageResult(CommonCode.SUCCESS, save);
        }
        //添加失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }*/

    /**
     * 异常处理
     * @param cmsPage
     * @return
     */
    @Override
    public CmsPageResult add(CmsPage cmsPage) {
        if (cmsPage == null) {
            //非法入参异常，指定异常信息的内容
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (cmsPage1 != null) {
            //页面已存在，无法新增
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }

        //调用dao新增页面
        cmsPage.setPageId(null);//保证mongodb帮助我们设置id，清空自己设的id
        CmsPage save = cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, save);


    }

    @Override
    public CmsPage getById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public CmsPageResult update(String id, CmsPage cmsPage) {
        //根据id查询页面信息
        CmsPage cmsPage1 = this.getById(id);
        if (cmsPage1 != null) {
            //设置修改信息
            cmsPage1.setTemplateId(cmsPage.getTemplateId());
            cmsPage1.setSiteId(cmsPage.getSiteId());
            cmsPage1.setPageAliase(cmsPage.getPageAliase());
            cmsPage1.setPageName(cmsPage.getPageName());
            cmsPage1.setPageWebPath(cmsPage.getPageWebPath());
            cmsPage1.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            cmsPage1.setDataUrl(cmsPage.getDataUrl());
            //执行更新
            CmsPage save = cmsPageRepository.save(cmsPage1);
            if (save != null) {
                CmsPageResult cmsPageResult = new CmsPageResult(CommonCode.SUCCESS, save);
                return cmsPageResult;

            }
        }
        //修改失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    //根据id删除页面
    @Override
    public ResponseResult deleteById(String id) {
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()) {
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    //根据id查询cmsConfig
    @Override
    public CmsConfig getConfigById(String id) {
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if (optional.isPresent()) {
            CmsConfig cmsConfig = optional.get();
            return cmsConfig;
        }
        return null;
    }

    //页面静态化流程

    /**
     * 1. 填写页面dataUrl
     *      在编辑cms页面信息界面填写DataUrl，将此字段保存到cms_page集合中
     * 2. 静态化程序获取页面的DataUrl
     * 3. 静态化程序远程请求DataUr获取数据模型
     * 4. 静态化程序获取页面的模板信息
     * 5. 执行页面静态化
     * @param pageId
     * @return
     */
    @Override
    public String getPageHtml(String pageId) {
        //3. 获取数据模型
        Map model = this.getModelByPageId(pageId);
        if (model == null) {
            //数据模型获取不到
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }

        //4. 获取页面的模板信息
        String template = this.getTemplateByPageId(pageId);
        if (StringUtils.isEmpty(template)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        //5. 执行静态化
        String html = generateHtml(template, model);

        return html;

    }

    //发布页面
    @Override
    public ResponseResult post(String pageId) {
        //执行页面静态化
        String pageHtml = this.getPageHtml(pageId);
        //将页面静态化文件存储到GridFS中
        CmsPage cmsPage = this.saveHtml(pageId, pageHtml);
        //向MQ发消息
        this.sendPostPage(pageId);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    //保存页面，有则更新，没有则添加
    @Override
    public CmsPageResult save(CmsPage cmsPage) {
        //判断页面是否存在
        CmsPage one = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
        if (one != null) {
            //进行更新
            return this.update(one.getPageId(), cmsPage);
        }
        return this.add(cmsPage);
    }

    //一键发布
    @Override
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        //页面的信息存储到cms_page集合中
        CmsPageResult save = this.save(cmsPage);
        if (!save.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //得到页面Id
        CmsPage cmsPageSave = save.getCmsPage();
        String pageId = cmsPageSave.getPageId();
        //执行页面发布（先静态化、保存GridFS,向MQ发送消息）
        ResponseResult post = this.post(pageId);
        if (!post.isSuccess()) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //拼接页面URL=cmsSite.siteDomain + cmsSite.siteWebPath + cmsPage.pageWebPath + cmsPage.pageName
        //取出siteId站点id
        String siteId = cmsPageSave.getSiteId();
        CmsSite cmsSite = this.findCmsSiteById(siteId);
        //页面URL
        String pageUrl = cmsSite.getSiteDomain() + cmsSite.getSiteWebPath() + cmsPageSave.getPageWebPath() + cmsPageSave.getPageName();

        return new CmsPostPageResult(CommonCode.SUCCESS, pageUrl);
    }

    //根据站点id查询站点信息
    public CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> siteOptional = cmsSiteRepository.findById(siteId);
        if (siteOptional.isPresent()) {
            return siteOptional.get();
        }
        return null;
    }

    //向mq发送消息
    public void sendPostPage(String pageId) {
        //先得到页面信息 为获取routingKey（站点id）
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM); //查不到，说明pageId是错的
        }
        //创建消息对象
        Map<String, String> msg = new HashMap<>();
        msg.put("pageId", pageId);
        //转为json
        String jsonString = JSON.toJSONString(msg);
        //发送给mq
        //routingKey 站点id (同一站点的页面发送到 特定站点)
        String siteId = cmsPage.getSiteId();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, siteId, jsonString);

    }

    //保存html到GridFs
    private CmsPage saveHtml(String pageId, String htmlContent) {
        //先得到页面信息
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM); //查不到，说明pageId是错的
        }

        ObjectId objectId = null;
        //将html文件内容保存到GridFs
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.toInputStream(htmlContent, "utf-8");
            objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将html文件id更新到cmsPage中
        cmsPage.setHtmlFileId(objectId.toHexString()); //方法返回的字符串表示的无符号整数参数所表示的值以十六进制
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }

    //执行静态化
    private String generateHtml(String templateContent, Map model) {
        //创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateContent);
        //向configuration配置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //获取模板
        try {
            Template template = configuration.getTemplate("template");

            //调用api进行静态化
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //获取页面的模板信息
    private String getTemplateByPageId(String pageId) {
        //取出页面的信息
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }

        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()) {
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件id
            String templateFileId = cmsTemplate.getTemplateFileId();


            //从GridFS中获取模板文件内容
            //根据文件id查询文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开一个下载流
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);

            //从流中获取数据
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    //获取数据模型
    private Map getModelByPageId(String pageId) {
        //取出页面信息
        CmsPage cmsPage = this.getById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        String dataUrl = cmsPage.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            //页面URL为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }

        //通过restTemplate请求dataUrl获取数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        return body;
    }

}
