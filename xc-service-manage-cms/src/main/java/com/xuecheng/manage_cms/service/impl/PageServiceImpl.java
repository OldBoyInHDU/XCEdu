package com.xuecheng.manage_cms.service.impl;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.service.IPageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


@Service
public class PageServiceImpl implements IPageService {
    @Autowired
    private CmsPageRepository cmsPageRepository;

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
        Example<CmsPage> example = Example.of(condition);
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

    @Override
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
    }
}
