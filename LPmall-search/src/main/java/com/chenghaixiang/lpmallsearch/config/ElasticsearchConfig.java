package com.chenghaixiang.lpmallsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Configuration
public class ElasticsearchConfig {
    @Bean
    public RestHighLevelClient esResClient(){

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.10.100", 9200, "http")));

        return client;
    }

    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder=RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization","Bearer"+ TermVectorsResponse.TermVector.Token);
//        builder.setHttpAsyncResponseConsumerFactory();
        COMMON_OPTIONS=builder.build();
    }
}
