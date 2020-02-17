package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.ResponseResult;

/**
 * @author hh
 * @create 2020/2/7
 * @description
 **/
public interface ICourseService {

    //课程计划的查询
    public TeachplanNode findTeachPlanList(String courseId);

    //添加课程计划
    ResponseResult addTeachplan(Teachplan teachplan);

    //添加课程图片
    ResponseResult addCoursePic(String courseId, String pic);

    //查询课程图片
    CoursePic findCoursePic(String courseId);

    //删除课程图片
    ResponseResult deleteCoursePic(String courseId);

    //查询课程视图
    CourseView getCourseView(String id);

    //课程预览
    CoursePublishResult preview(String id);

    //课程发布
    CoursePublishResult publish(String id);
}
