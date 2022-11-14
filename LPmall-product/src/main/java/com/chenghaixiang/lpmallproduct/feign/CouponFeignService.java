package com.chenghaixiang.lpmallproduct.feign;

import com.chenghaixiang.common.to.SkuReductionTo;
import com.chenghaixiang.common.to.SpuBoundTo;
import com.chenghaixiang.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@FeignClient("LPmall-coupon")
public interface CouponFeignService {

    @RequestMapping("/lpmallcoupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    @RequestMapping("lpmallcoupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
