package com.chenghaixiang.lpmallsearch;

import com.chenghaixiang.lpmallsearch.config.ElasticsearchConfig;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
class LPmallSearchApplicationTests {
    @Autowired
    RestHighLevelClient client;

    @Test
    void contextLoads() {
        System.out.println(client);
    }

    @Test
    void searchClient() throws IOException {
        //创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("bank");
        // 构建检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("address","mill"));
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("ageAgg").field("age").size(10);
        searchSourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(searchSourceBuilder);

        // 响应数据
        SearchResponse searchResponse = client.search(searchRequest, ElasticsearchConfig.COMMON_OPTIONS);

        System.out.println(searchResponse.toString());
    }

}
