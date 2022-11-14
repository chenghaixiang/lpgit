package com.chenghaixiang.lpmallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.lpmallproduct.Vo.AttrGroupRelation;
import com.chenghaixiang.lpmallproduct.Vo.AttrResVo;
import com.chenghaixiang.lpmallproduct.Vo.AttrVo;
import com.chenghaixiang.lpmallproduct.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 14:18:13
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrResVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelation[] vos);


    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    // 在指定规格属性中找出可检索的
    List<Long> selectSearchAtts(List<Long> attrIds);
}

