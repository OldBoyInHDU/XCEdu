package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * @author hh
 * @create 2020/2/3
 * @description
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridTemplateTest {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    //存文件
    @Test
    public void testStore() throws FileNotFoundException {
        //要存储的文件
        File file = new File("D:\\prac_project\\day9\\course.ftl");
        //定义fileInputStream
        FileInputStream fileInputStream = new FileInputStream(file);

        ObjectId objectId = gridFsTemplate.store(fileInputStream, "course.ftl");
        System.out.println(objectId);
    }

    //取文件
    @Test
    public void testQueryFile() throws IOException {
        //根据文件id查询文件
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5e37c502197cfc0774b83969")));
        //打开一个下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        //创建GridFsResource对象，获取流
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);

        //从流中获取数据
        String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
        System.out.println(content);
    }

    //删除文件
    @Test
    public void testDelete() {
        //根据文件id删除fs.files和fs.chunks中的记录
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is("5e37c502197cfc0774b83969")));
    }
}
