package com.chenghaixiang.lpmallware.service.impl;

import com.chenghaixiang.lpmallware.Vo.MergeVo;
import com.chenghaixiang.lpmallware.Vo.PurchaseDoneVo;
import com.chenghaixiang.lpmallware.Vo.PurchaseItemDoneVo;
import com.chenghaixiang.lpmallware.entity.PurchaseDetailEntity;
import com.chenghaixiang.lpmallware.service.PurchaseDetailService;
import com.chenghaixiang.lpmallware.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.Query;

import com.chenghaixiang.lpmallware.dao.PurchaseDao;
import com.chenghaixiang.lpmallware.entity.PurchaseEntity;
import com.chenghaixiang.lpmallware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status",0).or().eq("status",1);

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if(purchaseId==null){
            PurchaseEntity purchaseEntity=new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(0);
            this.save(purchaseEntity);
            purchaseId=purchaseEntity.getId();
        }

        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(i);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(1);
            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    @Transactional
    // 领取采购单
    @Override
    public void received(List<Long> ids) {
        // 1. 确认当前采购单是新建还是已分配
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(id -> {
            if (id.getStatus() == 0 || id.getStatus() == 1) {
                return true;
            }
            return false;
        }).map(item -> {
            item.setStatus(2);
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());
        // 2.改变采购单状态
        this.updateBatchById(collect);
        // 3.改变采购项状态
        collect.forEach(item->{
            List<PurchaseDetailEntity> purchaseDetailEntity=purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> collect1 = purchaseDetailEntity.stream().map(entiy -> {
                PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
                detailEntity.setId(entiy.getId());
                detailEntity.setStatus(2);
                return detailEntity;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(collect1);
        });
    }


    // 完成采购
    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {
        Long id = doneVo.getId();

        // 2.改变采购项状态
        Boolean flag=true;

        List<PurchaseDetailEntity> updates=new ArrayList<>();
        List<PurchaseItemDoneVo> items = doneVo.getItems();
        for (PurchaseItemDoneVo item:items){
            System.out.println(item.toString());
        }
        for(PurchaseItemDoneVo item:items){
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if(item.getStatus()==4){
                flag=false;
                purchaseDetailEntity.setStatus(item.getStatus());
            }else {
                purchaseDetailEntity.setStatus(3);
                // 3.将成功采购的进行入库
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                System.out.println(entity.toString());
                wareSkuService.addStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum());
            }
            purchaseDetailEntity.setId(item.getItemId());
            updates.add(purchaseDetailEntity);
        }
        purchaseDetailService.updateBatchById(updates);

        // 1.改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag?3:4);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);

    }

}