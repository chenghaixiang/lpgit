package com.chenghaixiang.lpmallproduct.service.impl;

import com.chenghaixiang.lpmallproduct.service.CategoryBrandRelationService;
import com.chenghaixiang.lpmallproduct.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chenghaixiang.common.utils.PageUtils;
import com.chenghaixiang.common.utils.Query;

import com.chenghaixiang.lpmallproduct.dao.BrandDao;
import com.chenghaixiang.lpmallproduct.entity.BrandEntity;
import com.chenghaixiang.lpmallproduct.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key= (String) params.get("key");
        QueryWrapper<BrandEntity> brandEntityQueryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(key)){
            brandEntityQueryWrapper.eq("brand_id",key).or().like("name",key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                brandEntityQueryWrapper
        );

        return new PageUtils(page);
    }

    // 更新brand表时，同时更新与它想关联的表信息
    @Transactional
    @Override
    public void updateDetail(BrandEntity brand) {
        this.updateById(brand);
        if(!StringUtils.isEmpty(brand.getName())){
            categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());
        }
    }

}