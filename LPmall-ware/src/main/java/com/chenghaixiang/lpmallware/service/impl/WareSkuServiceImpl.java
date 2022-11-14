package com.chenghaixiang.lpmallware.service.impl;

import com.chenghaixiang.common.utils.R;
import com.chenghaixiang.lpmallware.Vo.SkuHasStockVo;
import com.chenghaixiang.lpmallware.feign.ProductionFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.Query;

import com.chenghaixiang.lpmallware.dao.WareSkuDao;
import com.chenghaixiang.lpmallware.entity.WareSkuEntity;
import com.chenghaixiang.lpmallware.service.WareSkuService;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    ProductionFeignService productionFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();

        String skuId=(String)params.get("skuId");
        if (!StringUtils.isEmpty(skuId)){
            wrapper.eq("sku_id",skuId);
        }
        String wareId=(String)params.get("wareId");
        if (!StringUtils.isEmpty(wareId)){
            wrapper.eq("ware_id",wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    // 采购完成添加库存
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (wareSkuEntities.isEmpty()){
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            R info = productionFeignService.info(skuId);
            System.out.println("草");
            System.out.println(info);
            if(info.getCode()==0){
                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");
                wareSkuEntity.setSkuName((String) data.get("skuName"));
            }

            wareSkuDao.insert(wareSkuEntity);
        }else {
            wareSkuDao.addStock(skuId,wareId, skuNum);
        }


    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> collect = skuIds.stream().map(sku -> {
            SkuHasStockVo vo = new SkuHasStockVo();
            Long count = baseMapper.getSkuStock(sku);
            vo.setSkuId(sku);
            vo.setHasStock(count==null?false:count>0);
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

}