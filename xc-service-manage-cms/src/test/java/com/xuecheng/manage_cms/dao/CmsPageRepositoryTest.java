package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 查找所有
     */
    @Test
    public void testFindAll() {
        List<CmsPage> pages = cmsPageRepository.findAll();
        pages.stream().forEach(a -> System.out.println(a));
    }

    /**
     * 分页查询 Pageable
     */
    @Test
     public void testFindPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CmsPage> pages = cmsPageRepository.findAll(pageable);
        System.out.println(pages);
    }

    /**
     * 修改
     *
     */
    @Test
    public void testUpadate() {
        //查询
        Optional<CmsPage> optional = cmsPageRepository.findById("5abefd525b05aa293098fca6");
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            //修改
            cmsPage.setPageAliase("测试页面01");
            //保存
            CmsPage save = cmsPageRepository.save(cmsPage);
            System.out.println(save);
        }
    }

    /**
     * 自定义方法测试
     *
     */
    @Test
    public void testFindByPageName() {
        CmsPage page = cmsPageRepository.findByPageName("index.html");
        System.out.println(page);
    }

    @Test
    public void testFindAllByExample() {
        //分页
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        //条件值对象
        CmsPage condition = new CmsPage();
        //要查询5a751fab6abb5044e0d19ea1站点的页面
//        condition.setSiteId("5a751fab6abb5044e0d19ea1");
        condition.setPageAliase("轮播");
        //条件匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching();
        exampleMatcher = exampleMatcher.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //定义Example
        Example<CmsPage> example = Example.of(condition, exampleMatcher);

        Page<CmsPage> pages = cmsPageRepository.findAll(example, pageable);
        List<CmsPage> content = pages.getContent();
        System.out.println(content);
    }

    @Test
    public void testRestTemplate() {
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        System.out.println(body);
    }
}
