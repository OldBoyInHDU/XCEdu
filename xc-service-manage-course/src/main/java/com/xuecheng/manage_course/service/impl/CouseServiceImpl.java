package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hh
 * @create 2020/2/7
 * @description
 **/
@Service
public class CouseServiceImpl implements ICourseService {

    @Autowired
    private TeachplanMapper teachplanMapper;
    @Override
    public TeachplanNode findTeachPlanList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }
}
