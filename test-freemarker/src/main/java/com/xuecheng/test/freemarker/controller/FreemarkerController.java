package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RequestMapping("/freemarker")
@Controller
public class FreemarkerController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/course")
    public String testCourse(Map<String, Object> map) {
        //使用restTemplate请求轮播图的模型数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31200/course/courseview/4028e581617f945f01617f9dabc40000", Map.class);
        Map body = forEntity.getBody();
        //设置模型数据
        map.putAll(body);
        return "course";
    }

    @RequestMapping("/banner")
    public String testBanner(Map<String, Object> map) {
        //使用restTemplate请求轮播图的模型数据
        ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f", Map.class);
        Map body = forEntity.getBody();
        //设置模型数据
        map.putAll(body);
        return "index_banner";
    }

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
