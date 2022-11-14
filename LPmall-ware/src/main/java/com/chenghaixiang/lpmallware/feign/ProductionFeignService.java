package com.chenghaixiang.lpmallware.feign;

import com.chenghaixiang.common.to.SkuReductionTo;
import com.chenghaixiang.common.to.SpuBoundTo;
import com.chenghaixiang.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@FeignClient("LPmall-product")
public interface ProductionFeignService {

    @RequestMapping("/lpmallproduct/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);
}
