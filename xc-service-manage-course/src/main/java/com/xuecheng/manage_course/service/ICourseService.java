package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;

/**
 * @author hh
 * @create 2020/2/7
 * @description
 **/
public interface ICourseService {

    //课程计划的查询
    public TeachplanNode findTeachPlanList(String courseId);
}
