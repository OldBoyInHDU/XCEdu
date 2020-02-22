package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;

/**
 * @author hh
 * @create 2020/2/22
 * @description
 **/
public interface IEsCourseService {
    //课程综合搜索
    QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam);
}
