package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import org.springframework.web.bind.annotation.PathVariable;

public interface IPageService {
    //查询页面
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
    //新增页面
    public CmsPageResult add(CmsPage cmsPage);
}
