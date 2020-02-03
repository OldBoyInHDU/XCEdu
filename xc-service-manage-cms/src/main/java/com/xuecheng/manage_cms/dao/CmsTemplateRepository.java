package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hh
 * @create 2020/2/3
 * @description
 **/
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate, String> {
}
