package com.chenghaixiang.lpmallsearch.controller;

import com.chenghaixiang.common.to.es.SkuEsModel;
import com.chenghaixiang.common.utils.R;
import com.chenghaixiang.lpmallsearch.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@RestController
@RequestMapping("/search")
public class ElasticSaveController {
    @Autowired
    ProductService productService;
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) throws IOException {
        Boolean b=false;
        try {
            b=productService.productStatusUp(skuEsModels);
        }catch (Exception e){
            return R.error();
        }

        if (b) {
            return R.error().put("message", "商品上架失败");
        }else {
            return R.ok();
        }
    }
}
