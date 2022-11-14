package com.chenghaixiang.lpmallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.lpmallproduct.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 14:18:12
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(Long id, List<String> images);
}

