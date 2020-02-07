package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author hh
 * @create 2020/2/7
 * @description
 **/
@Api(value = "课程管理接口", description = "课程管理接口，提供数据模型的管理、查询接口")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);
}
