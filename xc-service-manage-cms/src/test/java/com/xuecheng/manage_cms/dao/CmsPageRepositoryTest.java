package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    private CmsPageRepository cmsPageRepository;

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
}
