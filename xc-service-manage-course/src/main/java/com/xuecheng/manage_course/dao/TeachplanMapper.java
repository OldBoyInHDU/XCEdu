package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author hh
 * @create 2020/2/7
 * @description
 **/
@Mapper
public interface TeachplanMapper {
    public TeachplanNode selectList(String courseId);
}
