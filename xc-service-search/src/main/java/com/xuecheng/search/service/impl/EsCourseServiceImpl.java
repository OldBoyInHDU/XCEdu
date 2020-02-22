package com.xuecheng.search.service.impl;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.search.service.IEsCourseService;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hh
 * @create 2020/2/22
 * @description
 **/
@Service
public class EsCourseServiceImpl implements IEsCourseService {

    @Value("${xuecheng.elasticsearch.course.index}")
    private String index;

    @Value("${xuecheng.elasticsearch.course.type}")
    private String type;

    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String source_field;

    @Autowired
    private RestHighLevelClient client;

    @Override
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {
        if (courseSearchParam == null) {
            courseSearchParam = new CourseSearchParam();
        }

        //创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest(index);
        //设置搜索类型
        searchRequest.types(type);
        
        //创建搜索源
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        
        //过滤源字段
        String[] source_field_array = source_field.split(",");
        searchSourceBuilder.fetchSource(source_field_array, new String[]{});

        //创建一个Bool查询对象
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //搜索条件
        //根据关键字搜索
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
            MultiMatchQueryBuilder multiMatch = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(), "name", "description", "teachplan")
                    .minimumShouldMatch("70%")
                    .field("name", 10);
            boolQueryBuilder.must(multiMatch);
        }

        //根据分类 使用过滤器
        if (StringUtils.isNotEmpty(courseSearchParam.getMt())) {
            //一级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("mt", courseSearchParam.getMt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())) {
            //二级分类
            boolQueryBuilder.filter(QueryBuilders.termQuery("st", courseSearchParam.getSt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
            //难度等级
            boolQueryBuilder.filter(QueryBuilders.termQuery("grade", courseSearchParam.getGrade()));
        }


        //装载搜索源
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        QueryResult queryResult = new QueryResult();
        List<CoursePub> list = new ArrayList<>();
        //执行搜索
        try {
            //执行搜索
            SearchResponse searchResponse = client.search(searchRequest);
            //获得响应数据
            SearchHits hits = searchResponse.getHits();
            //匹配的总记录数
            long totalHits = hits.getTotalHits();
            queryResult.setTotal(totalHits);

            for (SearchHit hit : hits) {
                CoursePub coursePub = new CoursePub();
                //源文档
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                //取出name
                String name = (String) sourceAsMap.get("name");
                coursePub.setName(name);
                //取出图片
                String pic = (String) sourceAsMap.get("pic");
                coursePub.setPic(pic);
                //取出价格
                Double price = null;
                try {
                    if (sourceAsMap.get("price") != null) {
                        price = (Double) sourceAsMap.get("price");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice(price);

                Double price_old = null;
                try {
                    if (sourceAsMap.get("price_old") != null) {
                        price_old = (Double) sourceAsMap.get("price_old");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                coursePub.setPrice_old(price_old);

                //添加到list
                list.add(coursePub);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        queryResult.setList(list);

        QueryResponseResult<CoursePub> queryResponseResult = new QueryResponseResult<>(CommonCode.SUCCESS, queryResult);
        return queryResponseResult;
    }
}
