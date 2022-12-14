package com.chenghaixiang.lpmallproduct.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chenghaixiang.common.to.SkuReductionTo;
import com.chenghaixiang.common.to.SpuBoundTo;
import com.chenghaixiang.common.to.es.SkuEsModel;
import com.chenghaixiang.common.utils.R;
import com.chenghaixiang.lpmallproduct.Vo.JsonVo.*;
import com.chenghaixiang.lpmallproduct.Vo.SkuHasStockVo;
import com.chenghaixiang.lpmallproduct.dao.SpuImagesDao;
import com.chenghaixiang.lpmallproduct.dao.SpuInfoDescDao;
import com.chenghaixiang.lpmallproduct.entity.*;
import com.chenghaixiang.lpmallproduct.feign.CouponFeignService;
import com.chenghaixiang.lpmallproduct.feign.SearchFeignService;
import com.chenghaixiang.lpmallproduct.feign.WareFeignService;
import com.chenghaixiang.lpmallproduct.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.Query;

import com.chenghaixiang.lpmallproduct.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    SpuInfoDescDao spuInfoDescDao;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    // ??????????????????????????????????????????????????????SPU
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {
        // 1.??????spu????????????  pms_spu_info
        SpuInfoEntity infoEntity=new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo,infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);

        // 2.??????spu??????????????? pms_spu_info_desc
        List<String> decript=spuSaveVo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(infoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        this.saveSpuInfoDesc(spuInfoDescEntity);

        // 3.??????spu????????????  pms_spu_images
        List<String> images = spuSaveVo.getImages();
        spuImagesService.saveImages(infoEntity.getId(),images);

        // 4.??????spu???????????????  pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(attr.getAttrId());
            AttrEntity byId = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(byId.getAttrName());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setSpuId(infoEntity.getId());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);

        // 5.??????spu??????????????? sms_spu_bounds
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(infoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if(r.getCode()!=0){
            log.error("????????????");
        }

        // 6.????????????spu?????????sku??????
        // 6.1  sku???????????????: pms_sku_info
        List<Skus> skus = spuSaveVo.getSkus();
        if(!skus.isEmpty()){
            skus.forEach(item->{
                String defaultImg="";
                for(Images a:item.getImages()){
                    if(a.getDefaultImg()==1){
                        defaultImg=a.getImgUrl();
                    }
                }

                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveInfo(skuInfoEntity);

                Long skuId=skuInfoEntity.getSkuId();
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity->{
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                // 6.2  sku??????????????? pms_sku_images
                skuImagesService.saveBatch(imagesEntities);

                // 6.3 sku???????????????  pms_sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntityList);

                // 6.4 sku????????????????????? sms_sku_ladder/sms_sku_full/sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                /**  ???????????????????????????????????????????????????????????????????????? **/
                if(skuReductionTo.getFullCount()>0||skuReductionTo.getFullPrice().compareTo(new BigDecimal(0))==1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if(r1.getCode()!=0){
                        log.error("????????????");
                    }
                }
            });
        }

    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity infoEntity) {
        this.baseMapper.insert(infoEntity);
    }

    @Override
    public void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity) {
        spuInfoDescDao.insert(spuInfoDescEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and(w->{
               w.eq("id",key).or().like("spu_name",key);
            });
        }

        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    // ????????????
    @Override
    public void up(Long spuId) throws IOException {

        // ????????????spu_id ?????????sku
        List<SkuInfoEntity> skuInfoEntities=skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIdList = skuInfoEntities.stream().map(sku -> {
            return sku.getSkuId();
        }).collect(Collectors.toList());

        // ??????????????????
        List<ProductAttrValueEntity> baseAttrs=productAttrValueService.baseAttrListforspu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        // ????????????????????????
        List<Long> searchsIds=attrService.selectSearchAtts(attrIds);

        Set<Long> idset=new HashSet<>(searchsIds);
        List<SkuEsModel.Attrs> attrs=new ArrayList<>();
        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> {
            return idset.contains(item.getAttrId());
        }).map(item -> {
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).collect(Collectors.toList());

        Map<Long, Boolean> stockMap=new HashMap<>();
        // ????????????????????????????????????????????????
        try {
            R skuHasStock = wareFeignService.getSkuHasStock(skuIdList);
            Object data = skuHasStock.get("data");
            // ??????????????????????????????
            JSONArray jsonArray = JSONArray.fromObject(data);
            List<SkuHasStockVo> stockVoList =new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
                skuHasStockVo.setSkuId(((Integer)jsonArray.getJSONObject(i).get("skuId")).longValue());
                skuHasStockVo.setHasStock((Boolean) jsonArray.getJSONObject(i).get("hasStock"));
                stockVoList.add(skuHasStockVo);
            }
            stockMap = stockVoList.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));
        }catch (Exception e) {
            log.error("??????????????????", e);
        }

        // ????????????
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> upProducts = skuInfoEntities.stream().map(sku -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);

            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuIamge(sku.getSkuDefaultImg());

            // ????????????????????????????????????????????????
            if (finalStockMap == null) {
                skuEsModel.setHasStock(true);
            } else {
                skuEsModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }

            // ??????????????????????????????
            BrandEntity brandEntity = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            skuEsModel.setHotScore(0L);

            CategoryEntity categoryEntity = categoryService.getById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogName(categoryEntity.getName());

            skuEsModel.setAtts(attrsList);
            return skuEsModel;
        }).collect(Collectors.toList());

        // ?????????elasticsearch??????
        R r = searchFeignService.productStatusUp(upProducts);

        if(r.getCode()==0){
            SpuInfoEntity infoEntity = new SpuInfoEntity();
            infoEntity.setId(spuId);
            infoEntity.setPublishStatus(1);
            infoEntity.setUpdateTime(new Date());

            UpdateWrapper<SpuInfoEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",infoEntity.getId());
            updateWrapper.set("publish_status",infoEntity.getPublishStatus());
            updateWrapper.set("update_time",infoEntity.getUpdateTime());
            baseMapper.update(infoEntity,updateWrapper);
        }
    }



}