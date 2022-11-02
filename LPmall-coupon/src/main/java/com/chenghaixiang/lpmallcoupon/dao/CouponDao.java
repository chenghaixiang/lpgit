package com.chenghaixiang.lpmallcoupon.dao;

import com.chenghaixiang.lpmallcoupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 16:45:37
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
