package com.chenghaixiang.lpmallproduct.dao;

import com.chenghaixiang.lpmallproduct.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 14:18:13
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> selectSearchAttsIds(@Param("attrIds") List<Long> attrIds);
}
