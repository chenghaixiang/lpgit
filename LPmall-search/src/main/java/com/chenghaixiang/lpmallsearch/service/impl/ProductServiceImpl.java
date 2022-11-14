package com.chenghaixiang.lpmallsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.chenghaixiang.common.to.es.SkuEsModel;
import com.chenghaixiang.lpmallsearch.config.ElasticsearchConfig;
import com.chenghaixiang.lpmallsearch.constant.EsConstant;
import com.chenghaixiang.lpmallsearch.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@Slf4j
@Service("productService")
public class ProductServiceImpl implements ProductService {
    @Autowired
    RestHighLevelClient client;

    @Override
    public Boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        // 保存到es
        // 1.给es建立索引

        // 保存数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String s = JSON.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);

            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = client.bulk(bulkRequest, ElasticsearchConfig.COMMON_OPTIONS);

        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        if(!b){
            log.info("商品上架成功:{}",collect);
        }

        return b;
    }
}
