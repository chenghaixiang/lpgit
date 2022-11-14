package com.chenghaixiang.lpmallware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.lpmallware.Vo.SkuHasStockVo;
import com.chenghaixiang.lpmallware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 17:09:52
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
}

