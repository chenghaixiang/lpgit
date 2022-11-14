package com.chenghaixiang.lpmallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.lpmallproduct.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 14:18:13
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateDetail(BrandEntity brand);
}

