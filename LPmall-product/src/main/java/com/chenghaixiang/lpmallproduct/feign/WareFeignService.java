package com.chenghaixiang.lpmallproduct.feign;

import com.chenghaixiang.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@FeignClient("LPmall-ware")
public interface WareFeignService {
    @RequestMapping("/lpmallware/waresku/hasstock")
    R getSkuHasStock(@RequestBody List<Long> skuIds);
}
