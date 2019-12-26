package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import org.springframework.web.bind.annotation.PathVariable;

public interface IPageService {
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
}
