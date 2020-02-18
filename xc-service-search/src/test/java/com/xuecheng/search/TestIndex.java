package com.xuecheng.search;


import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    //添加文档
    @Test
    public void testAddDoc() throws IOException {
        //准备文档
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("name", "spring cloud实战");
        jsonMap.put("description", "本课程主要从四个章节进行讲解：1.微服务架构入门。2.spring cloud 实战。3.实战spring。4.。。");
        jsonMap.put("studymodel", "201001");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonMap.put("timestamp", dateFormat.format(new Date()));
        jsonMap.put("price", 5.6f);

        //创建请求
        IndexRequest indexRequest = new IndexRequest("xc_course", "doc");
        //指定文档内容
        indexRequest.source(jsonMap);
        //通过client进行http请求
        IndexResponse indexResponse = client.index(indexRequest);
        //响应结果
        DocWriteResponse.Result result = indexResponse.getResult();

        System.out.println(result);

    }

    //查询文档
    @Test
    public void testGetDoc() throws IOException {
        //创建请求
        GetRequest getRequest = new GetRequest("xc_course", "doc", "O4dlV3AB70vpNQQ-8OJA");
        //发起请求
        GetResponse getResponse = client.get(getRequest);
        //得到文档内容
        Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
        System.out.println(sourceAsMap);
    }

    //更新文档
    @Test
    public void testUpdateDoc() throws IOException {
        //创建请求
        UpdateRequest updateRequest = new UpdateRequest("xc_course", "doc", "O4dlV3AB70vpNQQ-8OJA");
        //待更新的内容
        Map<String, String> map = new HashMap<>();
        map.put("name", "spring cloud实战修改后");
        updateRequest.doc(map);
        //发起请求
        UpdateResponse updateResponse = client.update(updateRequest);

        RestStatus status = updateResponse.status();
        System.out.println(status);
    }

    //根据Id删除文档
    @Test
    public void testDeleteDoc() throws IOException {
        //删除文档的Id
        String id = "O4dlV3AB70vpNQQ-8OJA";

        //创建请求
        DeleteRequest deleteRequest = new DeleteRequest("xc_course", "doc", id);

        //发起请求
        DeleteResponse deleteResponse = client.delete(deleteRequest);
        //响应结果
        DocWriteResponse.Result result = deleteResponse.getResult();

        System.out.println(result);
    }
}
