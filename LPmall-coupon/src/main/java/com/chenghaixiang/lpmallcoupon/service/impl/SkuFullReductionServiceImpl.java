package com.chenghaixiang.lpmallcoupon.service.impl;

import com.chenghaixiang.common.to.MemberPrice;
import com.chenghaixiang.common.to.SkuReductionTo;
import com.chenghaixiang.lpmallcoupon.entity.MemberPriceEntity;
import com.chenghaixiang.lpmallcoupon.entity.SkuLadderEntity;
import com.chenghaixiang.lpmallcoupon.service.MemberPriceService;
import com.chenghaixiang.lpmallcoupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.Query;

import com.chenghaixiang.lpmallcoupon.dao.SkuFullReductionDao;
import com.chenghaixiang.lpmallcoupon.entity.SkuFullReductionEntity;
import com.chenghaixiang.lpmallcoupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {
    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSkuReduction(SkuReductionTo reductionTo) {
        // 保存sku的满减打折优惠等等  sms_sku_ladder
        SkuLadderEntity skuLadderEntity=new SkuLadderEntity();
        skuLadderEntity.setSkuId(reductionTo.getSkuId());
        skuLadderEntity.setFullCount(reductionTo.getFullCount());
        skuLadderEntity.setDiscount(reductionTo.getDiscount());
        skuLadderEntity.setAddOther(reductionTo.getCountStatus());
        if(reductionTo.getFullCount()>0){
            skuLadderService.save(skuLadderEntity);
        }


        // 保存 sms_sku_full_reduction
        SkuFullReductionEntity reductionEntity=new SkuFullReductionEntity();
        System.out.println("************");
        System.out.println(reductionTo!=null);
        System.out.println(reductionTo.getMemberPrice());
        System.out.println("**************");
        BeanUtils.copyProperties(reductionTo,reductionEntity);
        if(reductionEntity.getFullPrice().compareTo(new BigDecimal(0))==1){
            this.save(reductionEntity);
        }


        // 保存sms_member_price
        List<MemberPrice> memberPrice = reductionTo.getMemberPrice();
        List<MemberPriceEntity> collect = memberPrice.stream().map(item -> {
            MemberPriceEntity priceEntity = new MemberPriceEntity();
            priceEntity.setSkuId(reductionTo.getSkuId());
            priceEntity.setMemberLevelId(item.getId());
            priceEntity.setMemberLevelName(item.getName());
            priceEntity.setMemberPrice(item.getPrice());
            priceEntity.setAddOther(1);
            return priceEntity;
        }).filter(item->{
            return item.getMemberPrice().compareTo(new BigDecimal(0))==1;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(collect);
    }

}