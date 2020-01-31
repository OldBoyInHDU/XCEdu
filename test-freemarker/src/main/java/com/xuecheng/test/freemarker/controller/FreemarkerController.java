package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@RequestMapping("/freemarker")
@Controller
public class FreemarkerController {

    @RequestMapping("/test1")
    public String test1(Map<String, Object> map) {
        //map形参存在request域中，是freemarker模板使用的数据
        map.put("name", "张三");

        Student stu1 = new Student();
        stu1.setAge(18);
        stu1.setBirthday(new Date());
        stu1.setMoney(200F);
        stu1.setName("小红");

        Student stu2 = new Student();
        stu2.setAge(19);
        stu2.setBirthday(new Date());
        stu2.setMoney(100F);
        stu2.setName("小方");

        List<Student> friends = new ArrayList<>();
        friends.add(stu1);

        stu2.setFriends(friends);

        stu2.setBestFriend(stu1);

        List<Student> stus = new ArrayList<>();
        stus.add(stu1);
        stus.add(stu2);
        //将学生列表放在数据模型中
        map.put("stus", stus);

        Map<String, Student> stuMap = new HashMap<>();
        stuMap.put("stu1", stu1);
        stuMap.put("stu2", stu2);

        //向数据模型放数据
        map.put("stu1", stu1);
        map.put("stuMap", stuMap);

        //存放数字
        map.put("point", 123456789);


        //返回freemarker模板的位置，基于resource/templates路径
        return "test1";
    }
}
