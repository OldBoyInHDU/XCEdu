package com.xuecheng.test.freemarker;

import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author hh
 * @create 2020/1/31
 * @description
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class FreemarkerTest {

    //测试静态化，基于ftl模板生成html文件

    @Test
    public void testGenerateHtml() throws IOException, TemplateException {
        //1. 定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //2. 定义模板
        //2.1 得到classpath的路径
        String classpath = this.getClass().getResource("/").getPath();
        //2.2 定义模板路径
        configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates/"));
        //2.3 获取模板文件的内容
        Template template = configuration.getTemplate("test1.ftl");
        //3. 定义数据模型
        Map map = getMap();

        //4. 静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
//        System.out.println(content);

        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("d:/prac_project/test1.html"));

        //输出文件
        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();

    }

    //根据模板内容(字符串)生成html
    @Test
    public void testGenerateHtmlByString() throws IOException, TemplateException {
        //1. 定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //2. 定义模板
        //2.1 模板内容（字符串）
        String templateString = "" +
                "<html>\n" +
                "    <head></head>\n" +
                "    <body>\n" +
                "    名称：${name}\n" +
                "    </body>\n" +
                "</html>";
        //2.2 使用模板加载器将字符串变为模板
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("template", templateString);
        //2.3 在配置中设置模板加载器
        configuration.setTemplateLoader(stringTemplateLoader);
        //2.4 获得模板
        Template template = configuration.getTemplate("template", "utf-8");

        //3. 定义数据模型
        Map map = getMap();

        //4. 静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
//        System.out.println(content);

        InputStream inputStream = IOUtils.toInputStream(content);
        FileOutputStream outputStream = new FileOutputStream(new File("d:/prac_project/test1.html"));

        //输出文件
        IOUtils.copy(inputStream, outputStream);
        inputStream.close();
        outputStream.close();

    }

    //获取数据模型
    public Map getMap() {
        Map map = new HashMap();
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

        return map;
    }
}
