package com.chenghaixiang.lpmallorder.dao;

import com.chenghaixiang.lpmallorder.entity.OrderOperateHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单操作历史记录
 * 
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 17:02:39
 */
@Mapper
public interface OrderOperateHistoryDao extends BaseMapper<OrderOperateHistoryEntity> {
	
}
