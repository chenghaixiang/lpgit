package com.chenghaixiang.lpmallproduct.dao;

import com.chenghaixiang.lpmallproduct.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 14:18:13
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
