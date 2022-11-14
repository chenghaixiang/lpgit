package com.chenghaixiang.lpmallproduct.feign;

import com.chenghaixiang.common.to.es.SkuEsModel;
import com.chenghaixiang.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@FeignClient("LPmall-search")
public interface SearchFeignService {
    @PostMapping("/search/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) throws IOException ;
}
