package com.xuecheng.test.freemarker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/freemarker")
@Controller
public class FreemarkerController {

    @RequestMapping("/test1")
    public String test1(Map<String, Object> map) {
        //map形参存在request域中，是freemarker模板使用的数据
        map.put("name", "张三");

        //返回freemarker模板的位置，基于resource/templates路径
        return "test1";
    }
}
