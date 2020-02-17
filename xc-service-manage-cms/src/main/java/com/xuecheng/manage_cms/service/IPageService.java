package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.web.bind.annotation.PathVariable;

public interface IPageService {
    //查询页面
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
    //新增页面
    public CmsPageResult add(CmsPage cmsPage);

    //根据页面id查询页面信息
    public CmsPage getById(String id);
    //修改页面
    public CmsPageResult update(String id, CmsPage cmsPage);

    //根据页面id删除页面
    public ResponseResult deleteById(String id);

    //根据ID查询cmsConfig
    public CmsConfig getConfigById(String id);

    //根据pageId取模板，取数据，执行页面静态化
    public String getPageHtml(String pageId);

    //页面发布
    public ResponseResult post(String pageId);

    //保存页面
    CmsPageResult save(CmsPage cmsPage);

    //一键发布
    CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
