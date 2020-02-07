package com.xuecheng.manage_course.service.impl;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseBaseRepository;
import com.xuecheng.manage_course.dao.TeachplanMapper;
import com.xuecheng.manage_course.dao.TeachplanRepository;
import com.xuecheng.manage_course.service.ICourseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author hh
 * @create 2020/2/7
 * @description
 **/
@Service
public class CouseServiceImpl implements ICourseService {

    @Autowired
    private TeachplanRepository teachplanRepository;

    @Autowired
    private CourseBaseRepository courseBaseRepository;

    @Autowired
    private TeachplanMapper teachplanMapper;

    //课程计划的查询
    @Override
    public TeachplanNode findTeachPlanList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    //添加课程计划
    @Override
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null ||
                StringUtils.isEmpty(teachplan.getCourseid()) ||
                StringUtils.isEmpty(teachplan.getPname())
        ) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }

        //课程计划
        String courseId = teachplan.getCourseid();
        //parentId
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)) {
            //不填就是根节点
            //取出该课程的根节点
            parentid = this.getTeachplanRoot(courseId);
        }

        //查询父节点
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        Teachplan teachplanParent = optional.get();
        //父节点的级别
        String grade = teachplanParent.getGrade();
        //新节点
        Teachplan teachplanNew = new Teachplan();
        //将页面提交的teachplan信息拷贝到teachplanNew对象中
        BeanUtils.copyProperties(teachplan, teachplanNew);
        teachplanNew.setCourseid(courseId);
        teachplanNew.setParentid(parentid);

        if (grade.equals("1")) {
            teachplanNew.setGrade("2");//级别，根据父节点的级别来设置
        } else {
            teachplanNew.setGrade("3");//级别，根据父节点的级别来设置
        }

        teachplanRepository.save(teachplanNew);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询课程的根节点，如果查询不到， 要自动添加根节点
    private String getTeachplanRoot(String courseId) {
        //根据 课程id 查到 根课程信息
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplanList == null || teachplanList.size() <= 0) {
            //查询不到，自动添加根节点
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setPname(courseBase.getName());
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();
        }

        //查询到了
        return teachplanList.get(0).getId();
    }
}
