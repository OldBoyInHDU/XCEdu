package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.IPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {
    @Autowired
    private IPageService pageService;

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        //暂时使用静态数据进行测试
        /*QueryResult queryResult = new QueryResult();
        List<CmsPage> list = new ArrayList<>();
        CmsPage page1 = new CmsPage();
        list.add(page1);
        page1.setPageName("测试页面");
        queryResult.setList(list);
        queryResult.setTotal(list.size());
        QueryResponseResult responseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);*/

        return pageService.findList(page, size, queryPageRequest);
    }

    @Override
    @PostMapping("/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById(@PathVariable("id") String id) {
        return pageService.getById(id);
    }

    @Override
    @PutMapping("/edit/{id}") //http方法中put表示更新
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return pageService.update(id, cmsPage);
    }

    @Override
    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return pageService.deleteById(id);
    }

    @Override
    @PostMapping("/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.post(pageId);
    }

    @Override
    @PostMapping("/save")
    public CmsPageResult save(@RequestBody CmsPage cmsPage) {
        return pageService.save(cmsPage);
    }

    //一键发布
    @Override
    @PostMapping("/postPageQuick")
    public CmsPostPageResult postPageQuick(@RequestBody CmsPage cmsPage) {
        return pageService.postPageQuick(cmsPage);
    }

}
