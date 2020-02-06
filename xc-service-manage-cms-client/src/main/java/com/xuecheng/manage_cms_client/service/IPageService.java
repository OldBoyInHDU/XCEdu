package com.xuecheng.manage_cms_client.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;

/**
 * @author hh
 * @create 2020/2/5
 * @description
 **/
public interface IPageService {
    //保存html页面到服务器物理路径
    public void savePageToServerPath(String pageId);

    //根据页面id查询页面信息
    public CmsPage findCmsPageById(String pageId);

    //根据站点id查询站点信息
    public CmsSite findCmsSiteById(String siteId);
}
