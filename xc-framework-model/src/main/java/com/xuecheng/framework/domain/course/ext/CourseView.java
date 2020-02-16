package com.xuecheng.framework.domain.course.ext;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author hh
 * @create 2020/2/16
 * @description
 **/
@Data
@NoArgsConstructor
@ToString
public class CourseView implements Serializable {
    private CourseBase courseBase; //课程基础信息
    private CoursePic coursePic; //课程图片信息
    private CourseMarket courseMarket; //课程营销信息
    private TeachplanNode teachplanNode; //教学计划
}
