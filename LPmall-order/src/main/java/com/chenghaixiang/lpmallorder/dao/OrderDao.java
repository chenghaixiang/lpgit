package com.chenghaixiang.lpmallorder.dao;

import com.chenghaixiang.lpmallorder.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 17:02:39
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
