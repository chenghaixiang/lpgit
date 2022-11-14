package com.chenghaixiang.lpmallproduct.service.impl;

import com.chenghaixiang.lpmallproduct.Vo.AttrGroupWithAttrsVo;
import com.chenghaixiang.lpmallproduct.entity.AttrEntity;
import com.chenghaixiang.lpmallproduct.service.AttrService;
import org.springframework.beans.BeanUtils;
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

import com.chenghaixiang.lpmallproduct.dao.AttrGroupDao;
import com.chenghaixiang.lpmallproduct.entity.AttrGroupEntity;
import com.chenghaixiang.lpmallproduct.service.AttrGroupService;
import org.springframework.util.StringUtils;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        if(catelogId==0){
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<AttrGroupEntity>());
            return new PageUtils(page);
        }else{
            String key= (String) params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId);
            if(StringUtils.hasLength(key)){
                System.out.println(key);
                wrapper.and(obj->{
                    obj.eq("attr_group_id",key).or().like("attr_group_name",key);
                });
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);

            return new PageUtils(page);
        }
    }


    /**
     * 获取分类下所有分组&关联属性
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        // 查询分组
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));

        // 查询所有属性
        List<AttrGroupWithAttrsVo> collect = attrGroupEntities.stream().map(group -> {
            AttrGroupWithAttrsVo attrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(group, attrsVo);
            List<AttrEntity> relationAttr = attrService.getRelationAttr(attrsVo.getAttrGroupId());
            attrsVo.setAttrs(relationAttr);
            return attrsVo;
        }).collect(Collectors.toList());
        return collect;
    }

}