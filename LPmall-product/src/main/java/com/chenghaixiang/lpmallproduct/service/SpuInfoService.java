package com.chenghaixiang.lpmallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.lpmallproduct.Vo.JsonVo.SpuSaveVo;
import com.chenghaixiang.lpmallproduct.entity.SpuInfoDescEntity;
import com.chenghaixiang.lpmallproduct.entity.SpuInfoEntity;

import java.io.IOException;
import java.util.Map;

/**
 * spu信息
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 14:18:12
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo spuSaveVo);

    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void up(Long spuId) throws IOException;


}

