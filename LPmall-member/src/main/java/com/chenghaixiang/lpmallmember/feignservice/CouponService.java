package com.chenghaixiang.lpmallmember.feignservice;

import com.chenghaixiang.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 程海翔
 * @school 石家庄铁道大学
 */
@FeignClient("LPmall-coupon")
public interface CouponService {

    @RequestMapping("lpmallcoupon/coupon/member/list")
    public R memerbercoupons();
}
