package com.xuecheng.search;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author hh
 * @create 2020/2/18
 * @description
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {
    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private RestClient restClient;

    //创建索引库
    @Test
    public void testCreacteIndex() throws IOException {
        //创建索引对象
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
        //设置参数
        createIndexRequest.settings(Settings.builder().put("number_of_shards", "1").put("number_of_replicas", "0"));
        //指定映射
        createIndexRequest.mapping("doc", "{\n" +
                "\t\"properties\": {\n" +
                "\t\t\"studymodel\": {\n" +
                "\t\t\t\"type\": \"keyword\"\n" +
                "\t\t},\n" +
                "\t\t\"name\":{\n" +
                "\t\t\t\"type\": \"keyword\"\t\n" +
                "\t\t},\n" +
                "\t\t\"description\":{\n" +
                "\t\t\t\"type\": \"text\",\t\n" +
                "\t\t\t\"analyzer\": \"ik_max_word\",\t\n" +
                "\t\t\t\"search_analyzer\": \"ik_smart\"\n" +
                "\t\t},\n" +
                "\t\t\"pic\":{\n" +
                "\t\t\t\"type\": \"text\",\n" +
                "\t\t\t\"index\": false\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}", XContentType.JSON);

        //操作索引客户端
        IndicesClient indicesClient = client.indices();
        //执行创建索引库
        CreateIndexResponse createIndexResponse = indicesClient.create(createIndexRequest);
        //得到响应
        boolean isSuccess = createIndexResponse.isAcknowledged();

        System.out.println(isSuccess);
    }

    //删除索引库
    @Test
    public void testDeleteIndex() throws IOException {
        //删除索引请求对象
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");

        //删除索引(发起删除请求)
        //操作索引的客户端
        IndicesClient indicesClient = client.indices();
        //执行删除
        DeleteIndexResponse deleteIndexResponse = indicesClient.delete(deleteIndexRequest);

        //删除索引响应结果
        boolean isSuccess = deleteIndexResponse.isAcknowledged();

        System.out.println(isSuccess);
    }
}
