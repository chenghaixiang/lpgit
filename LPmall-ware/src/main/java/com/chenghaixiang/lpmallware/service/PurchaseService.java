package com.chenghaixiang.lpmallware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.lpmallware.Vo.MergeVo;
import com.chenghaixiang.lpmallware.Vo.PurchaseDoneVo;
import com.chenghaixiang.lpmallware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author chenghaixiang
 * @email 179102891@qq.com
 * @date 2022-10-23 17:09:52
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo doneVo);
}

